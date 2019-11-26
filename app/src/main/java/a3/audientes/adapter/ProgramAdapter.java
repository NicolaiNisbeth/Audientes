package a3.audientes.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import a3.audientes.R;
import a3.audientes.models.Program;


public class ProgramAdapter extends RecyclerView.Adapter<a3.audientes.adapter.ProgramAdapter.MyViewHolder> {

    private final List<Program> programList;
    private final View.OnTouchListener onTouch;
    Context mcontext;
    Activity mactivity;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        public MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.programName);
        }
    }

    public ProgramAdapter(@NonNull List<Program> programList, @Nullable View.OnTouchListener onTouch, Activity mactivity) {
        this.programList = programList;
        this.onTouch = onTouch;
        this.mactivity = mactivity;
        mcontext= mactivity.getBaseContext();
    }

    @Override @NonNull
    public a3.audientes.adapter.ProgramAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
       if(viewType == 3){
           itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_createprogram, parent, false) ;
       }
       else{
           itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_program, parent, false) ;
       }
       itemView.setOnTouchListener(onTouch);
       itemView.setOnLongClickListener(v -> true);

       return new a3.audientes.adapter.ProgramAdapter.MyViewHolder(itemView);
    }
    @Override
    public int getItemViewType(int position) {
        return programList.get(position).getType();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Program program = programList.get(position);
        holder.title.setText(program.getName());
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

}

