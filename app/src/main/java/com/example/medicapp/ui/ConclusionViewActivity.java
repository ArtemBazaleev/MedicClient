package com.example.medicapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.medicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConclusionViewActivity extends AppCompatActivity {
    public static final String CONCLUSION_HEADER = "conclHeader";
    public static final String CONCLUSION_TEXT = "conclText";

    @BindView(R.id.conclusionViewHeader) TextView textViewHeader;
    @BindView(R.id.conclusionViewText) TextView textViewTxt;

    private String header;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion_view);
        ButterKnife.bind(this);
        if (getIntent().getExtras()!=null) {
            header = getIntent().getExtras().getString(CONCLUSION_HEADER, "Заключение");
            text = getIntent().getExtras().getString(CONCLUSION_TEXT, "");
            textViewHeader.setText(header);
            textViewTxt.setText(text);
        }
    }
}
