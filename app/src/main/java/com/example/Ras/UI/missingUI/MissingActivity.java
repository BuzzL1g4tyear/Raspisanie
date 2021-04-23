package com.example.Ras.UI.missingUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Ras.AuthorizationActivity;
import com.example.Ras.MainActivity;
import com.example.Ras.R;
import com.example.Ras.objects.AppDrawer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;
import java.util.List;

import static com.example.Ras.Utils.FirebaseHelperKt.AUTH;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_ORDER;
import static com.example.Ras.Utils.FirebaseHelperKt.MISSING_PERSONS;
import static com.example.Ras.Utils.FirebaseHelperKt.REF_DATABASE;
import static com.example.Ras.Utils.FunsKt.setActId;

public class MissingActivity extends AppCompatActivity {

    public AppDrawer mAppDrawer = null;

    AutoCompleteTextView dropDownMenu;
    NachoTextView orderChips, diseaseChips, statementChips, validReasonChips;
    FloatingActionButton add_btn, sent_btn, edit_btn;
    private boolean clicked = false;
    private ArrayAdapter<String> adapter = null;
    private String group, date;
    private List<String> dataOrderChip = new ArrayList<>();
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

    // TODO: picture
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

        sent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSentClicked();
            }
        });
    }

    private void onSentClicked() {
        getChipsText();
        sentToDatabase();
    }

    private void sentToDatabase() {
        group = dropDownMenu.getText().toString();
        REF_DATABASE.child(MISSING_PERSONS).child(date).child(group)
                .child(CHILD_ORDER)
                .push().setValue(dataOrderChip);
    }

    private void getChipsText() {

        for (Chip chip : orderChips.getAllChips()) {
            CharSequence text = chip.getText();
            dataOrderChip.add(text.toString());
            Log.d("MyLog", "getChipsText: "+dataOrderChip.size());
        }

//        dataDiseaseChip.addAll(diseaseChips.getChipValues());
//        dataStatementChip.addAll(statementChips.getChipValues());
//        dateReasonChip.addAll(validReasonChips.getChipValues());
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
        adapter = new ArrayAdapter<String>(this, R.layout.group_number, groupNumber);
        dropDownMenu.setAdapter(adapter);
        initTrigger(orderChips);
    }

    private void initTrigger(NachoTextView textView) {
        textView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        textView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
    }

    private void initFields() {
        date = mainActivity.SetDate();
        orderChips = findViewById(R.id.chipTextOrder);
        diseaseChips = findViewById(R.id.chipTextDisease);
        statementChips = findViewById(R.id.chipTextStatement);
        validReasonChips = findViewById(R.id.chipTextValidReason);
        add_btn = findViewById(R.id.Add_FB);
        sent_btn = findViewById(R.id.Sent_FB);
        edit_btn = findViewById(R.id.Upd_FB);
        dropDownMenu = findViewById(R.id.dropDownMenu);
        toolbar_Missing = (Toolbar) findViewById(R.id.toolbarMissing);
        mAppDrawer = new AppDrawer(this, toolbar_Missing);
        groupNumber = MainActivity.list;
    }

    private void initAnim() {
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
    }

    private void onAddClicked() {
        setAnimation(clicked);
        setVisibility(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            edit_btn.setVisibility(View.VISIBLE);
            sent_btn.setVisibility(View.VISIBLE);
        } else {
            edit_btn.setVisibility(View.INVISIBLE);
            sent_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            sent_btn.startAnimation(fromBottom);
            edit_btn.startAnimation(fromBottom);
            add_btn.startAnimation(rotateOpen);
        } else {
            sent_btn.startAnimation(toBottom);
            edit_btn.startAnimation(toBottom);
            add_btn.startAnimation(rotateClose);
        }
    }

    private void setClickable(boolean clicked) {
        if (!clicked) {
            sent_btn.setClickable(true);
            edit_btn.setClickable(true);
        } else {
            sent_btn.setClickable(false);
            edit_btn.setClickable(false);
        }
    }
}