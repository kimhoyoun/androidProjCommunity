package com.example.andprojcommunity.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andprojcommunity.R;
import com.example.andprojcommunity.adapter.FeedAdapter;
import com.example.andprojcommunity.model.FeedDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    Context context;

    Button btnSearch;

    EditText searchText;
    TextView resultText;

    ArrayList<FeedDTO> dtoList;

    RecyclerView recyclerView;
    FeedAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    InputMethodManager imm;

    public SearchFragment( Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        database = FirebaseDatabase.getInstance("https://androidproj-ab6fe-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference().child("DB");

        dtoList = new ArrayList<>();

        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        searchText = (EditText) view.findViewById(R.id.searchText);
        resultText = (TextView) view.findViewById(R.id.resultText);
        imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

        recyclerView = view.findViewById(R.id.search_rectclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        adapter = new FeedAdapter(dtoList);
        recyclerView.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = searchText.getText().toString();
                if(!userid.equals("")){

                    Query myQuery = databaseReference.child("Feeds").orderByChild("userID").equalTo(userid);

                    myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dtoList.clear();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                FeedDTO feed = snapshot.getValue(FeedDTO.class);
                                dtoList.add(feed);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

                }
            }
        });

        return view;
    }

}