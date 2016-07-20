package com.faayda.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.FAQAdapter;
import com.faayda.listeners.OnWebServiceResult;
import com.faayda.models.FAQModel;
import com.faayda.models.FAQResponse;
import com.faayda.network.NetworkManager;
import com.faayda.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by vinove on 10/8/15.
 */
public final class FaqFragment extends Fragment implements OnWebServiceResult {

    ListView listView;
    FAQAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.FAQ);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);

        View view = inflater.inflate(R.layout.faq_layout, container, false);
        listView = (ListView) view.findViewById(R.id.faq_list);
        adapter = new FAQAdapter(getActivity());
        listView.setAdapter(adapter);
        try {
            fetchFAQ();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
/*
    @Override
    public void onPause() {
        ((MainActivity) getActivity()).setTopBarTitle(Constants.FAQ);
        super.onPause();
    }*/


    /*  @Override
      public void onViewCreated(View view, Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          ((MainActivity) getActivity()).setTopBarTitle(Constants.FAQ);
  //        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.NOTIFICATION_ONLY);
          try {
              fetchFAQ();
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
  */
    private void fetchFAQ() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(Constants.KEY_METHOD, Constants.KEY_FAQ);
        HashMap<String, String> params = new HashMap<>();
        params.put(getString(R.string.postKey), obj.toString());
        NetworkManager networkManager = new NetworkManager(getActivity(), params, this, Constants.SERVICE_TYPE.FAQ);
        networkManager.callWebService(Constants.URL);
    }

    @Override
    public void onWebServiceResult(String result, Constants.SERVICE_TYPE type) {

        if (result == null) return;
        switch (type) {
            case FAQ:
                FAQResponse response = new Gson().fromJson(result, FAQResponse.class);
                switch (response.code) {
                    case 200:
                        for (FAQModel item : response.faq) {
                            adapter.addItem(item);
                        }
                        break;
                    case 500:

                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
