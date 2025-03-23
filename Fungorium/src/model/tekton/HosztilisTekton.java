package Fungorium.src.model.tekton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HosztilisTekton extends Tekton {
    		//splitRate es gombaTest beallito konstruktor
		public HosztilisTekton(double splitR, List<Tekton> neighborsList){
			super(splitR, neighborsList);
		}

        public HosztilisTekton(){
			super();
		}
}
