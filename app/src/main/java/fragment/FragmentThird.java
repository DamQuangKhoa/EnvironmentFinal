package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sky.environment.R;
import com.example.sky.environment.Start;

/**
 * Created by Sky on 26/07/2017.
 */

public class FragmentThird extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third,container,false);
        Button btnStart = (Button) view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v ->{
            Intent intent = new Intent(v.getContext(), Start.class);
            startActivity(intent);
        });
        return view;
    }
}
