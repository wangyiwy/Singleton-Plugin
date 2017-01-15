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
        PsiField[] psiFields = psiClass.getFields();
        for (PsiField f : psiFields) {
            if (f.getName() != null && f.getName().equals(psiField.getName())) {
                return true;
            }
        }
        return false;
    }

    protected boolean containMethod(PsiClass psiClass, PsiMethod psiMethod) {
        PsiMethod[] psiMethods = psiClass.getMethods();
        for (PsiMethod m : psiMethods) {
            if (m.getName().equals(psiMethod.getName())) {
                return true;
            }
        }
        return false;
    }

    protected boolean containClass(PsiClass psiClass, PsiClass innerClass) {
        PsiClass[] psiClasses = psiClass.getInnerClasses();
        for (PsiClass p : psiClasses) {
            if (p.getName() != null && p.getName().equals(innerClass.getName())) {
                return true;
            }
        }
        return false;
    }
}
