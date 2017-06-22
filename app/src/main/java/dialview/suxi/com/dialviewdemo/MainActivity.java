package dialview.suxi.com.dialviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DialViewGroup dialview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialview = (DialViewGroup) findViewById(R.id.dialview);

        dialview.setMaxNum(3000);
        dialview.setProgress(1800);
        dialview.setDuration(4000);
//        dialview.invalidate();

        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        dialview.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                dialview.setMaxNum(100);
                dialview.setProgress(50);
                dialview.setDuration(3000);

                //请求重新代用ViewGroup的Measure与Layout方法，使用invalidate只能调用ViewGroup的draw方法，一般情况下自定义ViewGroup不需要重写onDraw
                dialview.requestLayout();
                break;
            case R.id.btn2:
                dialview.setMaxNum(10000);
                dialview.setProgress(3000);
                dialview.setDuration(4000);
                dialview.requestLayout();
                break;
            case R.id.btn3:
                dialview.setMaxNum(8888);
                dialview.setProgress(6000);
                dialview.setDuration(2000);
                dialview.requestLayout();
                break;
        }

    }
}
