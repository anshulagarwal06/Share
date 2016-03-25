package com.sharesmile.share.gps;

import com.sharesmile.share.gps.models.DistRecord;
import com.sharesmile.share.gps.models.WorkoutData;

/**
 * Created by ankitm on 25/03/16.
 */
public interface WorkoutDataStore {

	void addRecord(DistRecord record);

	float getTotalDistance();

	long getBeginTimeStamp();

	void addSteps(int numSteps);

	int getTotalSteps();

	float getDistanceCoveredSinceLastResume();

	long getLastResumeTimeStamp();

	boolean coldStartAfterResume();

	void workoutPause();

	void workoutResume();

	boolean isWorkoutRunning();

	void persistWorkoutData();

	WorkoutData retrieveFromPersistentStorage();

	void clearPersistentStorage();

	WorkoutData clear();
}
