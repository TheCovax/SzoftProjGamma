package Fungorium.src.model.tekton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StabilTekton extends Tekton {

    		//splitRate es gombaTest beallito konstruktor
		public StabilTekton(double splitR, List<Tekton> neighborsList){
			super(splitR, neighborsList);
		}

        public StabilTekton(){
			super();
		}
}
