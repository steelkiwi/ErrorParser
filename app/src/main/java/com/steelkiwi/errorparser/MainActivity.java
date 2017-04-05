package com.steelkiwi.errorparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String errorExample = "{\n" +
            "  \"field1\": [\"error1\", \"error2\"],\n" +
            "  \"field2\": [\"error3\", \"error4\"],\n" +
            "  \"nested_fields\": [\n" +
            "    {\n" +
            "      \"field3\": [],\n" +
            "      \"field4\": [],\n" +
            "      \"non_fields_error\": [\"error5\", \"error6\"]\n" +
            "    },\n" +
            "    {\n" +
            "      \"field5\": [\"error7\", \"error8\"],\n" +
            "      \"field6\": [\"error9\", \"error10\"],\n" +
            "      \"non_fields_error1\": [\"error11\", \"error12\"]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"non_fields_error2\": [\"error13\", \"error14\"]\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Toast.makeText(this, Parser.simpleParsing(errorExample), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
