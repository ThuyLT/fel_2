package com.framgia.e_learningsimple.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.framgia.e_learningsimple.R;
import com.framgia.e_learningsimple.adapter.WordAdapter;
import com.framgia.e_learningsimple.constant.JsonKeyConstant;
import com.framgia.e_learningsimple.listener.EndlessRecyclerOnScrollListener;
import com.framgia.e_learningsimple.model.RequestHelper;
import com.framgia.e_learningsimple.model.ResponseHelper;
import com.framgia.e_learningsimple.network.ErrorNetwork;
import com.framgia.e_learningsimple.url.UrlJson;
import com.framgia.e_learningsimple.util.Answer;
import com.framgia.e_learningsimple.util.Category;
import com.framgia.e_learningsimple.util.DialogUtil;
import com.framgia.e_learningsimple.util.MyAsynctask;
import com.framgia.e_learningsimple.util.NetworkUtil;
import com.framgia.e_learningsimple.util.ValueName;
import com.framgia.e_learningsimple.util.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ThuyIT on 10/05/2016.
 */
public class WordListActivity extends Activity {
    private SharedPreferences mSharedPreferences;
    private ArrayList<Word> mWordsList = new ArrayList<Word>();
    private ArrayList<Category> mCategoriesList = new ArrayList<Category>();
    private ArrayList<String> mStatusList = new ArrayList<String>();
    private WordAdapter mWordAdapter;
    private ArrayAdapter<Category> mCategoryAdapter;
    private RecyclerView mRecycleViewWords;
    private Spinner mSpinnerCategory;
    private Spinner mSpinnerStatus;
    private ImageButton mBtnBack;
    private int mCurrentPage = 1;
    private EndlessRecyclerOnScrollListener mOnScrollListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        mCategoriesList = (ArrayList<Category>) getIntent().getSerializableExtra(JsonKeyConstant.KEY_CATEGORY_LIST);
        if (mCategoriesList.size() > 0) {
            initialize();
            setUpCategorySpinner();
            setUpStatusSpinner();
            setupWordRecycleView();
        } else {
            DialogUtil.showAlert(this,R.string.no_category_found);
        }
    }

    public void onBackPressed() {
        finish();
    }

    private void initialize() {
        mSharedPreferences = getSharedPreferences(JsonKeyConstant.USER_SHARED_PREF, Context.MODE_PRIVATE);
        NetworkUtil.sAuthToken = mSharedPreferences.getString(JsonKeyConstant.AUTHO_TOKEN_FIELD, null);
        mRecycleViewWords = (RecyclerView) findViewById(R.id.list_words);
        mBtnBack = (ImageButton) findViewById(R.id.btn_back);
        mSpinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        mSpinnerStatus = (Spinner) findViewById(R.id.spinner_status);
        mStatusList.add(JsonKeyConstant.KEY_ALL_WORD);
        mStatusList.add(JsonKeyConstant.KEY_LEARNED);
        mStatusList.add(JsonKeyConstant.KEY_NOT_LEARNED);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupWordRecycleView() {
        LinearLayoutManager mLayoutManager;
        mWordAdapter = new WordAdapter(this, mWordsList);
        mRecycleViewWords.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleViewWords.setLayoutManager(mLayoutManager);
        mRecycleViewWords.setAdapter(mWordAdapter);
        mOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore() {
                loadWordList();
            }
        };
        mRecycleViewWords.addOnScrollListener(mOnScrollListener);
    }

    private void loadWordList() {
        new ObtainWordList().execute();
    }

    private void setUpCategorySpinner() {
        mCategoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, mCategoriesList);
        mSpinnerCategory.setAdapter(mCategoryAdapter);
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeFilterHandler();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpStatusSpinner() {
        ArrayAdapter<String> mStatusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStatusList);
        mSpinnerStatus.setAdapter(mStatusAdapter);
        mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeFilterHandler();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void changeFilterHandler() {
        mWordsList.clear();
        mCurrentPage = 1;
        mOnScrollListener.reset();
        mWordAdapter.notifyDataSetChanged();
        loadWordList();
    }

    private class ObtainWordList extends AsyncTask<String, Void, Void> {
        private ProgressDialog mProgressDialog;
        private String mResponseBody;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(WordListActivity.this);
            mProgressDialog.setMessage(getString(R.string.msg_wait));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... args) {
            mResponseBody = readTextFile(getResources().openRawResource(R.raw.words));
            return null;
        }

        public String readTextFile(InputStream inputStream) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte buf[] = new byte[1024];
            int len;
            try {
                while ((len = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {

            }
            return outputStream.toString();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            try {
                updateWordList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void updateWordList() throws JSONException {
            JSONArray wordsJson = new JSONObject(mResponseBody).optJSONArray(JsonKeyConstant.WORDS);
            int numOfWords = wordsJson.length();
            for (int i = 0; i < numOfWords; i++) {
                String mWordContent = wordsJson.optJSONObject(i).optString(JsonKeyConstant.CONTENT);
                JSONArray answersJson = wordsJson.optJSONObject(i).optJSONArray(JsonKeyConstant.ANSWERS);
                ArrayList<Answer> mAnswers = new ArrayList<Answer>();
                int numOfAnswers = answersJson.length();
                for (int j = 0; j < numOfAnswers; j++) {
                    String answerContent = answersJson.optJSONObject(j).optString(JsonKeyConstant.CONTENT);
                    Boolean isCorrect = answersJson.optJSONObject(j).optBoolean(JsonKeyConstant.IS_CORRECT);
                    mAnswers.add(new Answer(answerContent, isCorrect));
                }
                mWordsList.add(new Word(mWordContent, mAnswers));
            }
            mWordAdapter.notifyDataSetChanged();
        }
    }
}
