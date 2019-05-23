package com.example.medicapp.model;

public class ExerciseModel {
    private String urlImage = "https://img.webmd.com/dtmcms/live/webmd/consumer_assets/site_images/articles/health_tools/7_most_effective_exercises_slideshow/webmd_photo_of_trainer_walking_on_treadmill.jpg";
    private boolean isLoadingIndicator = false;

    public boolean isLoadingIndicator() {
        return isLoadingIndicator;
    }

    public void setLoadingIndicator(boolean loadingIndicator) {
        isLoadingIndicator = loadingIndicator;
    }



    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

}
