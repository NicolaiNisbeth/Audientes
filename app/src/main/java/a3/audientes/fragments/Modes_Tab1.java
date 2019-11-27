package a3.audientes.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a3.audientes.R;
import a3.audientes.adapter.ProgramAdapter;
import a3.audientes.models.Program;
import utils.AnimBtnUtil;

public class Modes_Tab1 extends Fragment implements View.OnTouchListener {


    private List<Program> programList = new ArrayList<>();
    private ProgramAdapter mAdapter;
    private final Map<Button, a3.audientes.fragments.Program> btnProgramMap = new HashMap<>();
    private final List<a3.audientes.fragments.Program> default_programs = new ArrayList<>();
    private final List<a3.audientes.fragments.Program> user_programs = new ArrayList<>();


    private final int ONE_SECONDS = 600;
    private View currentBtn, addbtn, mbtn1, mbtn2, mbtn3, mbtn4;
    private final Handler long_pressed_handler = new Handler();
    private Runnable longPressed = () -> { onLongClick(currentBtn); longClick = true; };
    private boolean longClick;

    public Modes_Tab1() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new Modes_Tab1();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //TODO for testing only
        addfakelist();

        View rod = inflater.inflate(R.layout.fragment_tab1, container, false);
        setupbuttons(rod);
        setupRecyclerView(rod);

        return rod;
    }



    // Denne metode kaldes når der bliver trykket på en knap
    private void onClick(View view) {
        System.out.println("short click");
    }

    // Denne metode kaldes når der bliver holdt en knap nede i over 2 sekunder
    private void onLongClick(View v) {
        AnimBtnUtil.bounceSlow(v, getActivity());

        System.out.println("Long click");
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_popup_edit_program, null);
        Button button1 =  (Button)dialogView.findViewById(R.id.button1);
        Button button2=  (Button)dialogView.findViewById(R.id.button2);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Cancel");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Yes");
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        if(v.getId() == R.id.createProgram){
            AnimBtnUtil.bounce(v, getActivity());
        }
        else{

        }

         */
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) currentBtn = v;

        long_pressed_handler.postDelayed(longPressed, ONE_SECONDS);
        if(event.getAction() == MotionEvent.ACTION_UP){
            long_pressed_handler.removeCallbacks(longPressed);

            if(!longClick) onClick(v);

            longClick = false;
        }

        return false;
    }

    public void setupbuttons(View rod){
        addbtn = rod.findViewById(R.id.addprogram_btn);
        mbtn1 = rod.findViewById(R.id.mainbtn1);
        mbtn2 = rod.findViewById(R.id.mainbtn2);
        mbtn3 = rod.findViewById(R.id.mainbtn3);
        mbtn4 = rod.findViewById(R.id.mainbtn4);

        addbtn.setOnTouchListener(this);
        mbtn1.setOnTouchListener(this);
        mbtn2.setOnTouchListener(this);
        mbtn3.setOnTouchListener(this);
        mbtn4.setOnTouchListener(this);

    }
    public void setupRecyclerView(View rod){
        RecyclerView recyclerView = rod.findViewById(R.id.programRecycler);
        mAdapter = new ProgramAdapter(programList, this, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
    public void addfakelist(){
        programList.add(new a3.audientes.models.Program("test1",1,1,1,1,1,1,false));
        programList.add(new a3.audientes.models.Program("test2",1,1,1,1,1,1,false));
        programList.add(new a3.audientes.models.Program("test3",1,1,1,1,1,1,false));
        programList.add(new a3.audientes.models.Program("test4",1,1,1,1,1,1,false));
        programList.add(new a3.audientes.models.Program("+",1,1,1,1,1,3,false));

    }
}
