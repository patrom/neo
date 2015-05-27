package neo.objective.voiceleading;

import java.util.ArrayList;
import java.util.List;

import neo.util.Util;

public class VoiceLeadingSize {

	private List<Integer> source = new ArrayList<>();
	private List<Integer> target = new ArrayList<>();
	private int size;
	private int mod = 12;
	
	public List<Integer> getSource() {
		return source;
	}
	
	public void addAllToSource(List<Integer> source) {
		this.source.addAll(source);
	}
	
	public List<Integer> getTarget() {
		return target;
	}
	
	public void addAllToTarget(List<Integer> target) {
		this.target.addAll(target);
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void addPitchClassToSource(Integer pitchClass){
		source.add(pitchClass);
	}
	
	public void addPitchClassToTarget(Integer pitchClass){
		target.add(pitchClass);
	}
	
	public void calculateSize(){
		int total = 0;
		for (int i = 0; i < source.size(); i++) {
			int diff = Math.abs(source.get(i) - target.get(i));
			int ic = Util.intervalClass(diff);
			total = total + ic;
		}
		this.size = total - (Util.intervalClass(Math.abs(source.get(0) - target.get(0))));
	}
	
	public List<Integer> getVlSource() {
		List<Integer> list = new ArrayList<>(source);
		if (list.get(0) == target.get(0)) {
			list.remove(0);
		}else{
			list.remove(source.size() - 1);
		}
		return list;
	}
	
	public List<Integer> getVlTarget() {
		List<Integer> list = new ArrayList<>(target);
		if (source.get(0) == target.get(0)) {
			list.remove(0);
		}else{
			list.remove(target.size() - 1);
		}
		return list;
	}
     
}
