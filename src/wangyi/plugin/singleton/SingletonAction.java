package wangyi.plugin.singleton;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import wangyi.plugin.singleton.builder.*;

/**
 * author WangYi
 * created on 2017/1/14.
 */
public class SingletonAction extends BaseGenerateAction implements OnPatternSelectedListener {
    public SingletonAction() {
        super(null);
    }

    private AnActionEvent mEvent;

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.mEvent = e;
        PatternPickerDialog dialog = new PatternPickerDialog();
        dialog.setOnPatternSelectedListener(this);
        dialog.setVisible(true);
    }

    @Override
    public void onPatternSelected(int index) {
        if (mEvent == null) return;

        BaseBuilder builder = null;
        switch (index) {
            case 0:
                builder = new EnumPatternBuilder();
                break;
            case 1:
                builder = new LazyPatternBuilder();
                break;
            case 2:
                builder = new HungryPatternBuilder();
                break;
            case 3:
                builder = new ThreadSafePatternBuilder();
                break;
            case 4:
                builder = new InnerClassPatternBuilder();
                break;
        }

        if (builder != null) {
            builder.build(mEvent);
        }
    }
}
