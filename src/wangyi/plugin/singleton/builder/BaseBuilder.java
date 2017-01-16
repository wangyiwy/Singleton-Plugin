package wangyi.plugin.singleton.builder;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

/**
 * author WangYi
 * created on 2017/1/14.
 */
public abstract class BaseBuilder {
    private AnActionEvent mEvent;

    public BaseBuilder(AnActionEvent event) {
        this.mEvent = event;
    }

    public abstract void build();

    public AnActionEvent getActionEvent() {
        return mEvent;
    }

    protected boolean containFiled(PsiClass psiClass, PsiField psiField) {
        return psiClass.findFieldByName(psiField.getName(), true) != null;
    }

    protected boolean containMethod(PsiClass psiClass, PsiMethod psiMethod) {
        return psiClass.findMethodsByName(psiMethod.getName(), true).length > 0;
    }

    protected boolean containClass(PsiClass psiClass, PsiClass innerClass) {
        return psiClass.findInnerClassByName(innerClass.getName(), true) != null;
    }
}
