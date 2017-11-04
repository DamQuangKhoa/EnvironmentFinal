package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.sky.environment.R;

/**
 * Created by Sky on 16/10/2017.
 */

public class User {
    private String name,address,email;
    private Bitmap image,rank;
    private int grade;
    private Context mcontext;

    public User(Context context) {
        mcontext = context;
         setGrade(100);
        this.email = context.getResources().getString(R.string.email);
        this.name = context.getResources().getString(R.string.nav_yourname);
        this.address = context.getResources().getString(R.string.nav_your_address);
        this.image = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_super);
    }

    public User(String name, String address, String email, Bitmap image,Context mcontext) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.image = image;
        grade =0;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", image=" + image +
                ", rank=" + rank +
                ", grade=" + grade +
                ", mcontext=" + mcontext +
                '}';
    }

    public Bitmap getRank() {
        return rank;
    }

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void setRank(Bitmap rank) {
        this.rank = rank;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
        switch (grade){
            case 0:
                rank = BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_rank0);
                break;
            case 100:
                rank = BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_rank1);
                break;
            case 200:
                rank = BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_rank2);
                break;
            case 300:
                rank = BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_rank3);
                break;
            case 400:
                rank = BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_rank4);
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
