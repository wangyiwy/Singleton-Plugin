package wangyi.plugin.singleton.builder;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * author WangYi
 * created on 2017/1/15.
 */
public class InnerClassPatternBuilder extends BaseBuilder {
    @Override
    public void build(Editor editor, PsiElementFactory elementFactory, Project project, PsiClass psiClass, String className) {
        PsiClass innerClass = createStaticInnerClass(className, project, elementFactory);
        if (!containClass(psiClass, innerClass)) {
            psiClass.add(innerClass);
        }

        if (psiClass.getConstructors().length == 0) {
            PsiMethod constructor = elementFactory.createConstructor();
            constructor.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
            psiClass.add(constructor);
        }
        String methodText = buildMethodText(className);
        PsiMethod psiMethod = elementFactory.createMethodFromText(methodText, psiClass);
        if (!containMethod(psiClass, psiMethod)) {
            psiClass.add(psiMethod);
        }
    }

    /**
     * 创建一个静态内部类
     */
    private PsiClass createStaticInnerClass(String className, Project project, PsiElementFactory elementFactory) {
        PsiClass innerClass = elementFactory.createClass(buildInnerClassName());
        PsiModifierList classModifierList = innerClass.getModifierList();
        if (classModifierList != null) {
            classModifierList.setModifierProperty(PsiModifier.PRIVATE, true);
            classModifierList.setModifierProperty(PsiModifier.STATIC, true);
        }
        PsiType psiType = PsiType.getTypeByName(className, project
                , GlobalSearchScope.EMPTY_SCOPE);
        PsiField psiField = elementFactory.createField(buildFiledText(), psiType);

        PsiModifierList filedModifierList = psiField.getModifierList();
        if (filedModifierList != null) {
            filedModifierList.setModifierProperty(PsiModifier.STATIC, true);
            filedModifierList.setModifierProperty(PsiModifier.FINAL, true);
        }

        PsiExpression psiInitializer = elementFactory.createExpressionFromText(buildInitializerText(className), psiField);
        psiField.setInitializer(psiInitializer);
        innerClass.add(psiField);

        return innerClass;
    }

    private String buildMethodText(String className) {
        return "public static " + className + " getInstance() {\n" +
                "        return SingletonHolder.INSTANCE;\n" +
                "    }";
    }

    private String buildInnerClassName() {
        return "SingletonHolder";
    }

    private String buildFiledText() {
        return "INSTANCE";
    }

    private String buildInitializerText(String className) {
        return "new " + className + "()";
    }
}
