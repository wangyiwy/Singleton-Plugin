package wangyi.plugin.singleton.builder;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * author WangYi
 * created on 2017/1/14.
 */
public class LazyPatternBuilder extends BaseBuilder {
    @Override
    public void build(Editor editor, PsiElementFactory elementFactory, Project project, PsiClass psiClass, String className) {
        //成员变量类型
        PsiType psiType = PsiType.getTypeByName(className, project
                , GlobalSearchScope.EMPTY_SCOPE);
        PsiField psiField = elementFactory.createField(buildFiledText(), psiType);

        //创建成员变量
        if (!containFiled(psiClass, psiField)) {
            PsiModifierList modifierList = psiField.getModifierList();
            if (modifierList != null) {
                modifierList.setModifierProperty(PsiModifier.STATIC, true);
            }
            psiClass.add(psiField);
        }

        //创建构造方法
        if (psiClass.getConstructors().length == 0) {
            PsiMethod constructor = elementFactory.createConstructor();
            constructor.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
            psiClass.add(constructor);
        }

        //创建方法
        String methodText = buildMethodText(className);
        PsiMethod psiMethod = elementFactory.createMethodFromText(methodText, psiClass);
        if (!containMethod(psiClass, psiMethod)) {
            psiClass.add(psiMethod);
        }
    }

    private String buildMethodText(String className) {
        return "public static " + className + " getInstance(){\n" +
                "        if (" + buildFiledText() + " == null){\n" +
                "           " + buildFiledText() + " = new " + className + "();\n" +
                "        }\n" +
                "        return " + buildFiledText() + ";\n" +
                "    }";
    }

    private String buildFiledText() {
        return "mInstance";
    }
}
