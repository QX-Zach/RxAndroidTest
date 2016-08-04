package personal.lingchen.demo.rxandroidtest.Http;

import java.util.List;

import personal.lingchen.demo.rxandroidtest.DouBan.Subject;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by ozner_67 on 2016/8/1.
 */
public interface HttpService {
    /**
     * @param start
     * @param count
     *
     * @return
     */
    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start")int start, @Query("count") int count);
}
