package Fungorium.src.model.tekton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Fungorium.src.model.GombaTest;

public class StabilTekton extends Tekton {

    	/**
		 * splitRate es gombaTest beallito konstruktor
		 */
		public StabilTekton(double splitR, List<Tekton> neighborsList){
			super(splitR, neighborsList);
		}

        public StabilTekton(double splitR, GombaTest g){
			super(splitR, g);
		}

        public StabilTekton(){
			super();
		}
}
