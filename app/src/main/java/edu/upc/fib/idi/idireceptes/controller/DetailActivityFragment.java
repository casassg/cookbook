package edu.upc.fib.idi.idireceptes.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.data.ReceptaDBHelper;
import edu.upc.fib.idi.idireceptes.model.Recepta;
import edu.upc.fib.idi.idireceptes.repositories.ReceptaRepository;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView descrView = (TextView) view.findViewById(R.id.descripcio);

        ReceptaRepository cntrl = new ReceptaRepository(new ReceptaDBHelper(getContext()));
        Intent intent = getActivity().getIntent();

        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){
            long id = intent.getLongExtra(Intent.EXTRA_TEXT,-1);
            Recepta recepta = cntrl.get(id);
            descrView.setText(recepta.getDescription());
            getActivity().setTitle(recepta.getTitle());
        } else {
            descrView.setText("Recepta no existent!");
        }

        return view;
    }
}
