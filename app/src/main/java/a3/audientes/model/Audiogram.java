package a3.audientes.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "audiogram_table")
public class Audiogram {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @TypeConverters(AudiogramConverter.class)
    private ArrayList<Integer> x = new ArrayList<>();
    @TypeConverters(AudiogramConverter.class)
    private ArrayList<Integer> y = new ArrayList<>();

    public Audiogram() {}

    public void addIndex(int[] xy){
        x.add(xy[0]);
        y.add(xy[1]);
    }

    public ArrayList<int[]> getGraf(){
        ArrayList<int[]> temp = new ArrayList<>();
        for(int i = 0; i < x.size(); i++){
            int[] xy = {x.get(i), y.get(i)};
            temp.add(xy);
        }
        return temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getX() {
        return x;
    }

    public void setX(ArrayList<Integer> x) {
        this.x = x;
    }

    public ArrayList<Integer> getY() {
        return y;
    }

    public void setY(ArrayList<Integer> y) {
        this.y = y;
    }
}
