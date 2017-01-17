package wangyi.plugin.singleton.builder;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;

/**
 * author WangYi
 * created on 2017/1/15.
 */
public class EnumPatternBuilder extends BaseBuilder {

    @Override
    public void build(Editor editor, PsiElementFactory elementFactory, Project project, PsiClass psiClass, String className) {
        super.build(editor, elementFactory, project, psiClass, className);
        PsiEnumConstant enumConstant = elementFactory.createEnumConstantFromText("INSTANCE", psiClass);

        if (!containFiled(psiClass, enumConstant)) {
            psiClass.add(enumConstant);
        }
        if (!psiClass.isEnum()) {
            //todo 暂时没找到直接将class 修改成enum的api
            CodeStyleManager.getInstance(project).reformat(psiClass);
            String text = " class " + className;
            int index = editor.getDocument().getText().indexOf(text);
            if (index != -1) {
                PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
                Document document = editor.getDocument();
                documentManager.doPostponedOperationsAndUnblockDocument(document);
                document.replaceString(index, index + text.length(), " enum " + className);
                documentManager.commitDocument(document);

            }
        }
    }
}
