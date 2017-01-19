package wangyi.plugin.singleton.builder;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * author WangYi
 * created on 2017/1/15.
 */
public class HungryPatternBuilder extends BaseBuilder {
    @Override
    public void build(Editor editor, PsiElementFactory elementFactory, Project project, PsiClass psiClass, String className) {
        if (psiClass.getConstructors().length == 0) {
            PsiMethod constructor = elementFactory.createConstructor();
            constructor.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
            psiClass.add(constructor);
        }

        PsiType psiType = PsiType.getTypeByName(className, project
                , GlobalSearchScope.EMPTY_SCOPE);
        PsiField psiField = elementFactory.createField(buildFiledText(), psiType);

        if (!containFiled(psiClass, psiField)) {
            PsiModifierList modifierList = psiField.getModifierList();
            if (modifierList == null) return;
            modifierList.setModifierProperty(PsiModifier.STATIC, true);

            PsiExpression psiInitializer = elementFactory.createExpressionFromText(buildInitializerText(className), psiField);
            psiField.setInitializer(psiInitializer);
            psiClass.add(psiField);
        }

        String methodText = buildMethodText(className);
        PsiMethod psiMethod = elementFactory.createMethodFromText(methodText, psiClass);
        if (!containMethod(psiClass, psiMethod)) {
            psiClass.add(psiMethod);
        }
    }

    private String buildMethodText(String className) {
        return "public static " + className + " getInstance() {\n" +
                "        return " + buildFiledText() + ";\n" +
                "    }";
    }

    private String buildFiledText() {
        return "INSTANCE";
    }

    private String buildInitializerText(String className) {
        return "new " + className + "()";
    }
}
