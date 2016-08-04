package personal.lingchen.demo.rxandroidtest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import personal.lingchen.demo.rxandroidtest.DouBan.DouBanMovieTopActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private String[] imgUrls;
    private static final String TAG = "MainActivity";
    @InjectView(R.id.btn_rxjava)
    Button btnRxjava;
    @InjectView(R.id.btn_showImg)
    Button btnShowImg;
    @InjectView(R.id.btn_douban)
    Button btnDouban;
    @InjectView(R.id.iv_image)
    ImageView ivImage;
    @InjectView(R.id.tv_show)
    TextView tvShow;
    @InjectView(R.id.btn_glide)
    Button btnGlide;
    @InjectView(R.id.iv_glide)
    ImageView ivGlide;

    private MyObserver myObserver;
    private MySubscriber mySubscriber;
    private Observable mObservable;


    private String[] names = {"张欣德", "凌晨", "小四", "想", "思", "龙", "发"};
    private int imgIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initImageUrls();
        myObserver = new MyObserver();
        mySubscriber = new MySubscriber();
        mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello");
                subscriber.onNext("HI");
                subscriber.onNext("alha");
                subscriber.onCompleted();
            }
        });
        mObservable.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Log.d(TAG, "call: " + o.toString());
            }
        });
    }

    private void initImageUrls() {
        imgUrls = new String[]{
                "http://ww1.sinaimg.cn/large/610dc034jw1f566a296rpj20lc0sggoj.jpg",
                "http://ww3.sinaimg.cn/large/610dc034jw1f5byokn81tj20dw0hiwfe.jpg",
                "http://ac-olwhhm4o.clouddn.com/DPCY44vIYPjVPKNzfHjMdXd9bk27q0i1X2nIaO8Z"
        };
    }

    private static int rxjavabtnCount = 0;


    private void showImage(final int resId) {
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, resId);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ShowImg_onError: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "Error," + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        ivImage.setImageDrawable(drawable);
                    }
                });
    }


    //    @OnClick({R.id.btn_rxjava, R.id.btn_showImg, R.id.btn_douban})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_rxjava:
//                break;
//            case R.id.btn_showImg:
//                break;
//            case R.id.btn_douban:
//                break;
//        }
//    }
//
    @OnClick({R.id.btn_rxjava, R.id.btn_showImg, R.id.btn_douban, R.id.btn_glide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rxjava:
                tvShow.setText("");
                rxjavabtnCount++;

                mObservable.subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        tvShow.append(rxjavabtnCount + " " + o.toString() + "\n");
                        Log.e(TAG, "call: onNext:" + o.toString());
                    }
                }).unsubscribe();

                break;
            case R.id.btn_showImg:
                showImage(R.drawable.headimg_0);
                break;
            case R.id.btn_douban:
                startActivity(new Intent(MainActivity.this, DouBanMovieTopActivity.class));
                break;
            case R.id.btn_glide:

                Glide.with(this).load(imgUrls[imgIndex++ % imgUrls.length]).placeholder(R.drawable.headimg_0).animate(R.anim.alphain)
                        .into(ivGlide);
                break;
        }
    }


    private class MyObserver implements Observer<String> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String t) {
            Log.e(TAG, "MyObserver_onCreate:" + t);
            tvShow.append(t + "\n");
        }
    }

    private class MySubscriber extends Subscriber<String> {

        @Override
        public void onCompleted() {
            Log.e(TAG, "Subcriber_onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "Subcriber_onError: " + e.getMessage());
        }

        @Override
        public void onNext(String s) {
            Log.e(TAG, "Subcriber_onCreate:" + s);
            tvShow.append(s + "\n");
        }

        @Override
        public void onStart() {
            Log.e(TAG, "Subcriber_onStart");
            super.onStart();
        }
    }


    private void obTest() {
        tvShow.setText("");
        Observable.from(names).subscribe(mySubscriber);
//        Observable.just("first", "two", "three").subscribe(mySubscriber).unsubscribe();
//        Observable.just("nihao").subscribe(myObserver).unsubscribe();
    }


}
