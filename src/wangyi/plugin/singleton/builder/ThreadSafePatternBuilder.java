package wangyi.plugin.singleton.builder;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * author WangYi
 * created on 2017/1/15.
 */
public class ThreadSafePatternBuilder extends BaseBuilder {
    @Override
    public void build(Editor editor, PsiElementFactory elementFactory, Project project, PsiClass psiClass, String className) {
        super.build(editor, elementFactory, project, psiClass, className);

        PsiType psiType = PsiType.getTypeByName(className, project
                , GlobalSearchScope.EMPTY_SCOPE);
        PsiField psiField = elementFactory.createField(buildFiledText(), psiType);

        if (!containFiled(psiClass, psiField)) {
            PsiModifierList modifierList = psiField.getModifierList();
            if (modifierList != null) {
                modifierList.setModifierProperty(PsiModifier.STATIC, true);
                modifierList.setModifierProperty(PsiModifier.VOLATILE, true);
            }
            psiClass.add(psiField);
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

    private String buildMethodText(String className) {
        return "public static " + className + " getInstance() {\n" +
                "        if (" + buildFiledText() + " == null) {\n" +
                "            synchronized (" + className + ".class) {\n" +
                "                if (" + buildFiledText() + " == null) {\n" +
                "                    " + buildFiledText() + " = new " + className + "();\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return " + buildFiledText() + ";\n" +
                "    }";
    }

    private String buildFiledText() {
        return "mInstance";
    }
}
