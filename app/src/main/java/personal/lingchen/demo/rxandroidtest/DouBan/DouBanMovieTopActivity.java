package personal.lingchen.demo.rxandroidtest.DouBan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import personal.lingchen.demo.rxandroidtest.Http.HttpMethods;
import personal.lingchen.demo.rxandroidtest.R;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DouBanMovieTopActivity extends AppCompatActivity {
    private static final String TAG = "DouBanMovieTopActivity";
    @InjectView(R.id.tv_msg)
    TextView tvMsg;
    @InjectView(R.id.btn_retry)
    Button btnRetry;
    @InjectView(R.id.btn_reset)
    Button btnReset;

    private Subscriber<List<Subject>> subscriber;
    Subscription intervalSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dou_ban_top250);
        ButterKnife.inject(this);
        Log.i(TAG, "onCreate: +++++++++++");
        Log.d(TAG, "onCreate: ddddddddddd");
        Log.w(TAG, "onCreate: wwwwwwwwwww");
        Log.e(TAG, "onCreate: --------");
        intervalSub = Observable.interval(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.i(TAG, "call: Long___" + aLong);
            }
        });

    }


    /**
     * 获取电影列表
     */
    private void getMovies() {
        subscriber = new Subscriber<List<Subject>>() {
            ProgressDialog pgDialog;

            @Override
            public void onStart() {
                pgDialog = ProgressDialog.show(DouBanMovieTopActivity.this, null, "正在加载...");

                super.onStart();
            }

            @Override
            public void onCompleted() {
                if (pgDialog != null) {
                    pgDialog.dismiss();
                    pgDialog = null;
                }
                Toast.makeText(DouBanMovieTopActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                tvMsg.setText(e.getMessage());
            }

            @Override
            public void onNext(List<Subject> movieEntity) {
                tvMsg.setText(movieEntity.toString());
            }
        };
        HttpMethods.getInstance().getTopMovie(subscriber, 0, 5);

        //取消订阅，即可取消网络请求
//        if (!subscriber.isUnsubscribed()) {
//            subscriber.unsubscribe();
//        }


//        String baseUrl = "https://api.douban.com/v2/movie/";
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//        HttpService httpService = retrofit.create(HttpService.class);
//        httpService.getTypMovie(0, 10).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MovieEntity>() {
//                    @Override
//                    public void onCompleted() {
//                        Toast.makeText(DouBanMovieTopActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        tvMsg.setText("Err:" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(MovieEntity movieEntity) {
//                        tvMsg.setText(movieEntity.toString());
//                    }
//                });


//        HttpService httpService = retrofit.create(HttpService.class);
//        Call<MovieEntity> call = httpService.getTypMovie(0, 10);
//        call.enqueue(new Callback<MovieEntity>() {
//            @Override
//            public void onResponse(Response<MovieEntity> response, Retrofit retrofit) {
//                Log.d(TAG, "getMovies_onResponse: " + response.body().toString());
//                tvMsg.setText(response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d(TAG, "getMovies_onFailure: " + t.getMessage());
//                tvMsg.setText("Error："+t.getMessage());
//            }
//        });

    }


    @OnClick({R.id.btn_retry, R.id.btn_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_retry:
                getMovies();
                break;
            case R.id.btn_reset:
                tvMsg.setText("");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (intervalSub != null && !intervalSub.isUnsubscribed()) {
            intervalSub.unsubscribe();
        }
        super.onDestroy();
    }
}
