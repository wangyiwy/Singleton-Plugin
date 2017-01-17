package wangyi.plugin.singleton.builder;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * author WangYi
 * created on 2017/1/14.
 */
public class BaseBuilder {
    public void build(AnActionEvent event) {
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
        if (psiFile == null) return;
        new WriteCommandAction.Simple(event.getProject(), psiFile) {
            @Override
            protected void run() throws Throwable {
                Editor editor = event.getData(PlatformDataKeys.EDITOR);
                if (editor == null) return;
                Project project = editor.getProject();
                if (project == null) return;

                PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
                PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
                if (psiClass == null) return;

                if (psiClass.getNameIdentifier() == null) return;
                String className = psiClass.getNameIdentifier().getText();

                PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

                build(editor, elementFactory, project, psiClass, className);
            }
        }.execute();
    }

    public void build(Editor editor, PsiElementFactory elementFactory, Project project, PsiClass psiClass, String className) {

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
