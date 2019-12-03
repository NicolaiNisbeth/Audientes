package a3.audientes.model;

import java.util.ArrayList;
import java.util.List;

import a3.audientes.view.adapter.ProgramAdapter;

public class ProgramManager {
    // static variable single_instance of type Singleton
    private static ProgramManager single_instance = null;

    private List<Program> programList = new ArrayList<>();
    public ProgramAdapter programadapter;


    // private constructor restricted to this class itself
    private ProgramManager() { }

    // static method to create instance of Singleton class
    public static ProgramManager getInstance() {
        if (single_instance == null)
            single_instance = new ProgramManager();

        return single_instance;
    }

    public void addProgram(Program program){
        programList.add(programList.size(), program);
        programadapter.notifyItemInserted(programList.size()-1);
    }

    public void deleteProgram(Program program){
        for(int i = 0; i < programList.size(); i++){
            if(program.getId() == programList.get(i).getId()){
                programList.remove(i);
                programadapter.notifyItemRemoved(i);
            }
        }
    }

    public void update(Program program){
        System.out.println(program.getName());
        System.out.println(program.getId());
        for(int i = 0; i < programList.size(); i++){
            if(program.getId() == programList.get(i).getId()){
                programList.get(i).setName(program.getName());
                programList.get(i).setLow(program.getLow());
                programList.get(i).setLow_plus(program.getLow_plus());
                programList.get(i).setMiddle(program.getMiddle());
                programList.get(i).setHigh(program.getHigh());
                programList.get(i).setHigh_plus(program.getHigh_plus());
                programadapter.notifyItemChanged(i);
            }
        }
    }

    public Program getProgram(int id){

        Program temp = null;

        for(int i = 0; i < programList.size(); i++){
            if(id == programList.get(i).getId()){
                temp = programList.get(i);
            }
        }
        return temp;
    }



    public List<Program> getProgramList() {
        return programList;
    }

    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }

    public int getNextId(){
        return programList.get(programList.size()-1).getId()+ 1;
    }

    public void setAdapter(ProgramAdapter programadapter){
        this.programadapter = programadapter;
    }
}