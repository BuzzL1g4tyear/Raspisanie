package com.example.Ras.UI.missingUI;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.Ras.MainActivity;
import com.example.Ras.R;
import com.example.Ras.models.User;
import com.example.Ras.objects.AppDrawer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_DISEASE;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_GROUP;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_ORDER;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_REASON;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_STATEMENT;
import static com.example.Ras.Utils.FirebaseHelperKt.CHILD_TIME;
import static com.example.Ras.Utils.FirebaseHelperKt.NODE_MISSING;
import static com.example.Ras.Utils.FirebaseHelperKt.NODE_USERS;
import static com.example.Ras.Utils.FirebaseHelperKt.REF_DATABASE;
import static com.example.Ras.Utils.FirebaseHelperKt.UID;
import static com.example.Ras.Utils.FirebaseHelperKt.USER;

public class CapitanMissingFragment extends Fragment {

    AutoCompleteTextView dropDownMenu;
    NachoTextView orderChips, diseaseChips, statementChips, validReasonChips;
    FloatingActionButton add_btn, sent_btn, edit_btn;
    private boolean clicked = false;
    private String group = "", date;
    private List<String> dataOrderChip = new ArrayList<>();
    private List<String> dataDiseaseChip = new ArrayList<>();
    private List<String> dataStatementChip = new ArrayList<>();
    private List<String> dataReasonChip = new ArrayList<>();

    private DatabaseReference mRefUser = REF_DATABASE.child(NODE_USERS);
    private final DatabaseReference path = REF_DATABASE.child(NODE_MISSING);
    MainActivity mainActivity = new MainActivity();

    private User user = USER;

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capitan_missing, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initFields();
        initFunc();
        initAnim();
        add_btn.setOnClickListener(v -> onAddClicked());

        sent_btn.setOnClickListener(v -> onSentClicked());
    }

    private void initFunc() {
        mRefUser.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    dropDownMenu.setText(user.getGroup());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        initTrigger(orderChips);
        initTrigger(diseaseChips);
        initTrigger(statementChips);
        initTrigger(validReasonChips);
    }

    private void onSentClicked() {
        getChipsText();
        sentToDatabase();
    }

    private void sentToDatabase() {
        group = dropDownMenu.getText().toString();
        if (group.isEmpty()) {
            Toast.makeText(requireContext(), "Группа не выбранна", Toast.LENGTH_SHORT).show();
        } else if (dataOrderChip.isEmpty() && dataDiseaseChip.isEmpty()
                && dataStatementChip.isEmpty() && dataReasonChip.isEmpty()) {
            Toast.makeText(requireContext(), "Нет ни одной записи", Toast.LENGTH_SHORT).show();
        } else {
            addMissingPersons();
        }
        dataOrderChip.clear();
        dataDiseaseChip.clear();
        dataStatementChip.clear();
        dataReasonChip.clear();
    }

    private void addMissingPersons() {
        Map<String, Object> missMap = new HashMap<>();

        missMap.put(CHILD_TIME, ServerValue.TIMESTAMP);
        missMap.put(CHILD_GROUP, USER.getGroup());
        missMap.put(CHILD_DISEASE, dataDiseaseChip);
        missMap.put(CHILD_REASON, dataReasonChip);
        missMap.put(CHILD_STATEMENT, dataStatementChip);
        missMap.put(CHILD_ORDER, dataOrderChip);

        path.child(date).child(UID).updateChildren(missMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getChipsText() {

        for (Chip chip : orderChips.getAllChips()) {
            CharSequence text = chip.getText();
            dataOrderChip.add(text.toString());
        }

        for (Chip chip : diseaseChips.getAllChips()) {
            CharSequence text = chip.getText();
            dataDiseaseChip.add(text.toString());
        }

        for (Chip chip : statementChips.getAllChips()) {
            CharSequence text = chip.getText();
            dataStatementChip.add(text.toString());
        }

        for (Chip chip : validReasonChips.getAllChips()) {
            CharSequence text = chip.getText();
            dataReasonChip.add(text.toString());
        }
    }

    private void initTrigger(NachoTextView textView) {
        textView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        textView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
    }

    private void initFields() {
        date = mainActivity.SetDate();
        orderChips = getView().findViewById(R.id.chipTextOrder);
        diseaseChips = getView().findViewById(R.id.chipTextDisease);
        statementChips = getView().findViewById(R.id.chipTextStatement);
        validReasonChips = getView().findViewById(R.id.chipTextValidReason);
        add_btn = getView().findViewById(R.id.Add_FB);
        sent_btn = getView().findViewById(R.id.Sent_FB);
        edit_btn = getView().findViewById(R.id.Upd_FB);
        dropDownMenu = getView().findViewById(R.id.dropDownMenu);
    }

    private void initAnim() {
        rotateOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim);
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