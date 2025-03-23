package Fungorium.src.model.tekton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ElszigeteltTekton extends Tekton{

		//splitRate es gombaTest beallito konstruktor
		public ElszigeteltTekton(double splitR, List<Tekton> neighborsList){
			super(splitR, neighborsList);
		}


		public ElszigeteltTekton(){
			super();
		}

}
