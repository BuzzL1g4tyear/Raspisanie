package com.example.Ras.UI.missingUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Ras.AuthorizationActivity;
import com.example.Ras.MainActivity;
import com.example.Ras.R;
import com.example.Ras.Sender;
import com.example.Ras.objects.AppDrawer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.Ras.Utils.FirebaseHelperKt.AUTH;
import static com.example.Ras.Utils.FunsKt.*;

public class MissingActivity extends AppCompatActivity {

    private AppDrawer mAppDrawer = null;
    private DatabaseReference dt_lessons;

    AutoCompleteTextView autoTextView;
    NachoTextView orderChips, diseaseChips, statementChips, validReasonChips;
    FloatingActionButton add_btn, sent_btn, cancel_btn;
    private boolean clicked = false;
    private String group;
    private List<String> dataChips = new ArrayList<>();
    private List<String> groupNumber = new ArrayList<>();
    Toolbar toolbar_Missing;

    MainActivity mainActivity = new MainActivity();

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing);
        setActId(2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initFields();
        checkUser();
        initAnim();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });
    }

    private void checkUser() {
        if (AUTH.getCurrentUser() != null) {

            initFunc();

        } else {
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private void initFunc() {
        setSupportActionBar(toolbar_Missing);
        mAppDrawer.create();
    }

    private void initFields() {
        orderChips = findViewById(R.id.chipTextOrder);
        diseaseChips = findViewById(R.id.chipTextDisease);
        statementChips = findViewById(R.id.chipTextStatement);
        validReasonChips = findViewById(R.id.chipTextValidReason);
        add_btn = findViewById(R.id.Add_FB);
        sent_btn = findViewById(R.id.Sent_FB);
        cancel_btn = findViewById(R.id.Upd_FB);
        autoTextView = findViewById(R.id.autoTextView);
        dt_lessons = FirebaseDatabase.getInstance().getReference();
        toolbar_Missing = (Toolbar) findViewById(R.id.toolbarMissing);
        mAppDrawer = new AppDrawer(this, toolbar_Missing);
    }

    private void initAnim() {
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getDate(mainActivity.SetDate());
        }
    };

    private void getDate(String date) {
        Query query = dt_lessons.child(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Sender sender = dataSnapshot.getValue(Sender.class);
                    assert sender != null;
                    groupNumber.add(sender.numbGroup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onAddClicked() {
        setAnimation(clicked);
        setVisibility(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            cancel_btn.setVisibility(View.VISIBLE);
            sent_btn.setVisibility(View.VISIBLE);
        } else {
            cancel_btn.setVisibility(View.INVISIBLE);
            sent_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            sent_btn.startAnimation(fromBottom);
            cancel_btn.startAnimation(fromBottom);
            add_btn.startAnimation(rotateOpen);
        } else {
            sent_btn.startAnimation(toBottom);
            cancel_btn.startAnimation(toBottom);
            add_btn.startAnimation(rotateClose);
        }
    }

    private void setClickable(boolean clicked) {
        if (!clicked) {
            sent_btn.setClickable(true);
            cancel_btn.setClickable(true);
        } else {
            sent_btn.setClickable(false);
            cancel_btn.setClickable(false);
        }
    }
}