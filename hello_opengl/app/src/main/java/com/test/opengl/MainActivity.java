package com.test.opengl;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.test.opengl.comtriangle.CommonTriangleActivity;
import com.test.opengl.egldemo.EglActivity;
import com.test.opengl.stl.konglong.KongLongSTLActivity;
import com.test.opengl.stl.threedimensional.Stl3DActivity;
import com.test.opengl.triangle.TriangleActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        findViewById(R.id.btn_triangle).setOnClickListener(this);

        findViewById(R.id.btn_com_triangle).setOnClickListener(this);

        findViewById(R.id.btn_3d).setOnClickListener(this);

        findViewById(R.id.btn_3d_konglong).setOnClickListener(this);

        findViewById(R.id.btn_triangle_elg).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_com_triangle:
                startComTriangleActivity();
                break;
            case R.id.btn_triangle:
                startTriangleActivity();
                break;
            case R.id.btn_triangle_elg:
                startEGLActivity();
                break;
            case R.id.btn_3d:
                startStl3DActivity();
                break;
            case R.id.btn_3d_konglong:
                startStlKonglong3DActivity();
                break;
        }
    }

    private void startTriangleActivity() {
        Intent intent = new Intent(this, TriangleActivity.class);
        startActivity(intent);
    }

    private void startComTriangleActivity() {
        Intent intent = new Intent(this, CommonTriangleActivity.class);
        startActivity(intent);
    }

    private void startEGLActivity() {
        Intent intent = new Intent(this, EglActivity.class);
        startActivity(intent);
    }

    private void startStl3DActivity() {
        Intent intent = new Intent(this, Stl3DActivity.class);
        startActivity(intent);
    }

    private void startStlKonglong3DActivity() {
        Intent intent = new Intent(this, KongLongSTLActivity.class);
        startActivity(intent);
    }
}
