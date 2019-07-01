package com.example.medicapp.model;

import com.example.medicapp.Constants;
import com.example.medicapp.networking.response.exercise.Exercise;

public class ExerciseModel {
    private String urlImage = "https://img.webmd.com/dtmcms/live/webmd/consumer_assets/site_images/articles/health_tools/7_most_effective_exercises_slideshow/webmd_photo_of_trainer_walking_on_treadmill.jpg";
    private boolean isLoadingIndicator = false;
    public static final int TYPE_VIDEO = 87;
    public static final int TYPE_HEADER = 51;
    private int type = TYPE_VIDEO;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExerciseModel() {
    }

    private String urlVideo = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private String name = "No Name";

    public boolean isLoadingIndicator() {
        return isLoadingIndicator;
    }

    public void setLoadingIndicator(boolean loadingIndicator) {
        isLoadingIndicator = loadingIndicator;
    }


    public ExerciseModel(Exercise exercise) {
        urlImage = Constants.BASE_URL_IMAGE + exercise.getVideos().get(0).getPreview();
        urlVideo = Constants.BASE_URL_IMAGE + exercise.getVideos().get(0).getVideo();
        name = exercise.getName();
        category = exercise.getCategory();
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

}
