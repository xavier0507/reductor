package com.yheriatovych.reductor.processor.combinedstate;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.*;
import com.yheriatovych.reductor.Commands;
import com.yheriatovych.reductor.Pair;
import com.yheriatovych.reductor.Reducer;
import com.yheriatovych.reductor.Store;
import com.yheriatovych.reductor.annotations.CombinedState;
import com.yheriatovych.reductor.processor.Env;
import com.yheriatovych.reductor.processor.MethodTypeInfo;
import com.yheriatovych.reductor.processor.Utils;
import com.yheriatovych.reductor.processor.ValidationException;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.yheriatovych.reductor.processor.Utils.*;

public class CombinedStateProcessingStep implements BasicAnnotationProcessor.ProcessingStep {

    private static final String REDUCER_SUFFIX = "Reducer";
    private final Env env;

    public CombinedStateProcessingStep(Env env) {
        this.env = env;
    }


    @Override
    public Set<? extends Class<? extends Annotation>> annotations() {
        return Collections.singleton(CombinedState.class);
    }

    @Override
    public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
        for (Element element : elementsByAnnotation.values()) {
            TypeElement combinedStateTypeElement = (TypeElement) element;

            try {
                CombinedStateElement combinedStateElement = CombinedStateElement.parseCombinedElement(combinedStateTypeElement, env);
                if (combinedStateElement == null) continue;

                ClassName stateClassName = emmitCombinedStateImplementation(combinedStateElement);
                emmitCombinedReducer(env, combinedStateElement, stateClassName);
            } catch (ValidationException ve) {
                env.printError(ve.getElement(), ve.getMessage());
            } catch (Exception e) {
                env.printError(element, "Internal processor error:\n" + e.getMessage());
                e.printStackTrace();
            }
        }
        return Collections.emptySet();
    }

    private ClassName emmitCombinedStateImplementation(CombinedStateElement combinedStateElement) throws IOException {
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        List<MethodSpec> methodSpecs = new ArrayList<>();
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        for (StateProperty property : combinedStateElement.properties) {
            TypeName returnTypeName = TypeName.get(property.stateType);
            FieldSpec fieldSpec = FieldSpec.builder(
                    returnTypeName, property.name, Modifier.PRIVATE, Modifier.FINAL
            ).build();
            MethodSpec spec = MethodSpec
                    .methodBuilder(property.name)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(returnTypeName)
                    .addAnnotation(Override.class)
                    .addStatement("return $N", property.name)
                    .build();
            methodSpecs.add(spec);
            fieldSpecs.add(fieldSpec);

            ParameterSpec parameterSpec = ParameterSpec.builder(returnTypeName, property.name).build();
            constructorBuilder.addParameter(parameterSpec);
            constructorBuilder.addStatement("this.$N = $N", fieldSpec, parameterSpec);
        }


        TypeSpec typeSpec = TypeSpec.classBuilder(combinedStateElement.stateTypeElement.getSimpleName().toString() + "Impl")
                .addSuperinterface(TypeName.get(combinedStateElement.stateTypeElement.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(constructorBuilder.build())
                .addMethods(methodSpecs)
                .addFields(fieldSpecs)
                .build();

        JavaFile javaFile = JavaFile.builder(env.getPackageName(combinedStateElement.stateTypeElement), typeSpec)
                .addFileComment(createGeneratedFileComment())
                .skipJavaLangImports(true)
                .build();
        javaFile.writeTo(env.getFiler());

        return ClassName.get(javaFile.packageName, typeSpec.name);
    }

    public static void emmitCombinedReducer(
            final Env env,
            CombinedStateElement combinedStateElement,
            ClassName stateClassName
    ) throws IOException {
        final String stateParam = "state";
        final String actionParam = "action";

        TypeName reducerActionType = combinedStateElement.getCombinedReducerActionType();

        String packageName = env.getPackageName(combinedStateElement.stateTypeElement);
        String combinedStateClassName = combinedStateElement
                .stateTypeElement
                .getSimpleName()
                .toString();
        ClassName combinedReducerClassName = ClassName.get(
                packageName, combinedStateClassName + REDUCER_SUFFIX
        );

        final TypeName combinedReducerReturnTypeName = TypeName.get(
                combinedStateElement.stateTypeElement.asType()
        );

        final TypeName reducerTypeName = ParameterizedTypeName.get(
                ClassName.get(Reducer.class), combinedReducerReturnTypeName
        );

        List<StateProperty> properties = combinedStateElement.properties;

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(combinedReducerClassName)
                .addSuperinterface(reducerTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        List<FieldSpec> reducerFields = new ArrayList<>();
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE);

        String LIST_OF_REDUCER_NAME = "reducers";
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listOfReducerType = ParameterizedTypeName.get(list, reducerTypeName);

        FieldSpec subReducerField = FieldSpec.builder(
                listOfReducerType, LIST_OF_REDUCER_NAME, Modifier.PRIVATE
        ).initializer("new $T<>()", arrayList).build();
        reducerFields.add(subReducerField);

        constructorBuilder.addParameter(listOfReducerType, LIST_OF_REDUCER_NAME)
                .addStatement("this.$N = $N", LIST_OF_REDUCER_NAME, LIST_OF_REDUCER_NAME);

        MethodTypeInfo mdInfo = StateProperty.getReduceMethodInfo();
        // AppState
        TypeName pairType1 = TypeName.get(combinedStateElement.stateTypeElement.asType());
        // Commands<AppState>
        TypeName pairType2 = ParameterizedTypeName.get(
                ClassName.get((Class<?>) ((ParameterizedType)mdInfo.getGenericsInReturnType()[1])
                        .getRawType()),
                pairType1
        );

        // Pair<AppState, Commands>
        final TypeName reducerReturnTypeName = ParameterizedTypeName.get(
                ClassName.get((Class<?>) ((ParameterizedType)mdInfo.getGenericReturnType()).getRawType()),
                TypeName.get(combinedStateElement.stateTypeElement.asType()),
                pairType2
        );

        TypeName listOfPairType2 = ParameterizedTypeName.get(list, pairType2);
        String REDUCER_NAME = "reducer";
        String PAIR_NAME = "pair";
        String PAIR_TYPE2_NAME = "cmds";

        CodeBlock dispatchingBlockBuilder = CodeBlock.builder()
                .addStatement("final $T $N = new $T<>()", listOfPairType2, PAIR_TYPE2_NAME, arrayList)
                .beginControlFlow("for ($T $N : $N)", reducerTypeName, REDUCER_NAME, LIST_OF_REDUCER_NAME)
                .addStatement("$T $N = $N.reduce($N, action)", reducerReturnTypeName, PAIR_NAME, REDUCER_NAME, stateParam)
                .addStatement("$N = $N.first", stateParam, PAIR_NAME)
                .beginControlFlow("if ($N.second != null)", PAIR_NAME)
                .addStatement("$N.add($N.second)", PAIR_TYPE2_NAME, PAIR_NAME)
                .endControlFlow()
                .endControlFlow()
                .build();

        // only 'Pair'
        Type reducerReturnTypeWithoutGeneric = mdInfo.getReturnType();
        MethodSpec reduceMethodSpec = MethodSpec.methodBuilder("reduce")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(reducerReturnTypeName)
                .addParameter(combinedReducerReturnTypeName, stateParam)
                .addParameter(reducerActionType, actionParam)
                .addCode(dispatchingBlockBuilder).addCode("\n")
                .addCode(CombinedStateProcessingStep.emitReturnBlock(
                        reducerReturnTypeWithoutGeneric, stateParam, PAIR_TYPE2_NAME
                ))
                .build();

        ClassName builderClassName = ClassName.get(
                combinedReducerClassName.packageName(),
                combinedReducerClassName.simpleName(),
                "Builder"
        );
        TypeSpec reducerBuilderTypeSpec = CombinedStateProcessingStep.createReducerBuilder(
                combinedStateElement,
                combinedReducerClassName,
                builderClassName,
                reducerTypeName
        );


        MethodSpec builderFactoryMethod = MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .returns(builderClassName)
                .addStatement("return new $T()", builderClassName)
                .build();

        typeSpecBuilder
                .addMethod(constructorBuilder.build())
                .addFields(reducerFields)
                .addMethod(reduceMethodSpec)
                .addMethod(builderFactoryMethod)
                .addType(reducerBuilderTypeSpec);

        TypeSpec typeSpec = typeSpecBuilder.build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .skipJavaLangImports(true)
                .addFileComment(createGeneratedFileComment())
                .build();
        javaFile.writeTo(env.getFiler());
    }

    private static CodeBlock emitReturnBlock(Type returnType, String stateParam, String PAIR_TYPE2_NAME) {
        CodeBlock.Builder runCommands = CodeBlock.builder();
        runCommands
                .beginControlFlow("for ($T cmd : $N)", Commands.class, PAIR_TYPE2_NAME)
                .addStatement("cmd.run(store)")
                .endControlFlow();

        TypeSpec commands = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(Commands.class))
                .addMethod(MethodSpec.methodBuilder("run")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(Store.class, "store")
                                .addCode(runCommands.build())
                                .build())
                .build();

        return CodeBlock.builder()
                .addStatement("return $T.create($N, $L)",
                        returnType,
                        stateParam,
                        commands
                ).build();
    }

    private static TypeSpec createReducerBuilder(
            CombinedStateElement combinedStateElement,
            ClassName combinedReducerClassName,
            ClassName builderClassName,
            TypeName reducerTypeName
    ) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC);

        builder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build());

        String REDUCER_NAME = "reducer";
        String LIST_OF_REDUCER_NAME = "reducers";
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listOfReducerType = ParameterizedTypeName.get(list, reducerTypeName);

        FieldSpec field = FieldSpec.builder(
                listOfReducerType, LIST_OF_REDUCER_NAME, Modifier.PRIVATE
        ).initializer("new $T<>()", arrayList).build();

        MethodSpec setter = MethodSpec.methodBuilder("addReducer")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClassName)
                .addParameter(reducerTypeName, REDUCER_NAME)
                .addStatement("$N.add($N)", LIST_OF_REDUCER_NAME, REDUCER_NAME)
                .addStatement("return this")
                .build();

        builder.addField(field);
        builder.addMethod(setter);

        MethodSpec.Builder buildMethodBuilder = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(combinedReducerClassName);
        buildMethodBuilder.addStatement("return new $T(" + LIST_OF_REDUCER_NAME + ")", combinedReducerClassName);

        builder.addMethod(buildMethodBuilder.build());

        return builder.build();
    }
}
