package ua.beloded.geotask.Controller;

import com.google.gson.JsonElement;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Query;
import ua.beloded.geotask.model.DirectionResults;

public interface RetrofiQuery {

    @POST("/maps/api/directions/json")
    Call<DirectionResults> getWay (@Query("origin") String origin, @Query("destination") String destination,@Query("mode") String mode, @Query("key") String key );
}
