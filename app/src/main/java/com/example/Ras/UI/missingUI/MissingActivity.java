package com.example.Ras.UI.missingUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.Ras.AuthorizationActivity;
import com.example.Ras.R;
import com.example.Ras.objects.AppDrawer;

import static com.example.Ras.Utils.FirebaseHelperKt.AUTH;
import static com.example.Ras.Utils.FirebaseHelperKt.USER;
import static com.example.Ras.Utils.FirebaseHelperKt.initUser;
import static com.example.Ras.Utils.FunsKt.setActId;

public class MissingActivity extends AppCompatActivity {

    public AppDrawer mAppDrawer = null;
    Toolbar toolbar_Missing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing);
        setActId(2);
    }

    // TODO: chip - picture
    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
        setTitle(R.string.missing);
    }

    private void checkUser() {
        if (AUTH.getCurrentUser() != null) {
            initUser(() -> null);
            Fragment fragmentCapitan = new CapitanMissingFragment();
            Fragment fragmentAdmin = new AdminMissingFragment();

            toolbar_Missing = findViewById(R.id.toolbarMissing);
            setSupportActionBar(toolbar_Missing);
            mAppDrawer = new AppDrawer(this, toolbar_Missing);
            mAppDrawer.create();

            if (USER.getStatus().equals("2")) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.data_container, fragmentCapitan, null)
                        .commit();
            } else if (USER.getStatus().equals("4")) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.data_container, fragmentAdmin, null)
                        .commit();
            }
        } else {
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}