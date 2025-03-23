package Fungorium.src.model;

import Fungorium.src.model.tekton.Tekton;

public class GombaFonal {
	
	Tekton src;	
	Tekton dst;
	String owner;

	public GombaFonal(Tekton s, Tekton d, String o){
		src = s;
		dst = d;
		owner = o;
	}

	public void clean(){

	}

	public boolean isOwner(String s){
		return owner.equals(s);
	}



}
