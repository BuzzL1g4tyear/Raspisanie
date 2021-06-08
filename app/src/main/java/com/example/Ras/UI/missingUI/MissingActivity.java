package com.example.Ras.UI.missingUI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.Ras.AuthorizationActivity;
import com.example.Ras.MainActivity;
import com.example.Ras.R;
import com.example.Ras.Utils.AppValueEventListener;
import com.example.Ras.models.User;
import com.example.Ras.objects.AppDrawer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;

import static com.example.Ras.Utils.FirebaseHelperKt.AUTH;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_DISEASE;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_ORDER;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_REASON;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_STATEMENT;
import static com.example.Ras.Utils.FirebaseHelperKt.NODE_MISSING;
import static com.example.Ras.Utils.FirebaseHelperKt.NODE_USERS;
import static com.example.Ras.Utils.FirebaseHelperKt.REF_DATABASE;
import static com.example.Ras.Utils.FirebaseHelperKt.UID;
import static com.example.Ras.Utils.FirebaseHelperKt.USER;
import static com.example.Ras.Utils.FirebaseHelperKt.initMissingPers;
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