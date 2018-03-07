package Bean;

import java.util.ArrayList;

public class TrajectoryDetail {
	private ArrayList<String> info;
	private String trajectories;
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ArrayList<String> getInfo() {
		return info;
	}
	public void setInfo(ArrayList<String> info) {
		this.info = info;
	}
	public String getTrajectories() {
		return trajectories;
	}
	public void setTrajectories(String trajectories) {
		this.trajectories = trajectories;
	}
}
