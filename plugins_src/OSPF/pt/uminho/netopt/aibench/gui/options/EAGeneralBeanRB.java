/**
* Copyright 2012-2017,
* Centro Algoritmi
* University of Minho
*
* This is free software: you can redistribute it and/or modify
* it under the terms of the GNU Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Public License for more details.
*
* You should have received a copy of the GNU Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
* 
* @author Vítor Pereira
*/
package pt.uminho.netopt.aibench.gui.options;

import java.util.ListResourceBundle;

public class EAGeneralBeanRB extends ListResourceBundle {

	public EAGeneralBeanRB() {
	}

	protected Object[][] getContents() {
		return (new Object[][] {
			    { "minWeight", "Minimum OSPF weight"},
			    { "minWeight.shortDescription", "Default value is 1."},
			    { "maxWeight", "Maximum OSPF weight" },
			    { "maxWeight.shortDescription", "Default value is 20."},
			    { "DefaultBandWidth", "Default Link Bandwidth" },
				{ "DefaultBandWidth.shortDescription", "Default link bandwidth when building a new topology."},
				{ "DefaultDelay", "Default Link Delay" },
				{ "DefaultDelay.shortDescription", "Default link delay when building a new topology."},

				{ "PDefault","Default p value"},
				{ "PDefault.shortDescription", "Default p value is 1."},
				{ "PMin","Minimum p-Value"},
				{ "PMin.shortDescription", "Minimum value for p optimization. This value depends on the assigned minimum link weight (default 0.1)"},
				{ "PMax","Maximum p-Value"},
				{ "PMax.shortDescription", "Maximum value for p optimization. This value depends on the assigned minimum link weight (default 10.0)"},
				{ "PIntScaller","p-Values integer scaller"},
				{ "PIntScaller.shortDescription", "Scalling factor when using p value optmization integer encoding.\n If the minimal p value is a real 0.1 and the scalling factor is 100, the minimal p value with integer encoding will be 0.1*100=10."},
				
				{ "DeftThreshold","NSP link usage threshold"},
				{ "DeftThreshold.shortDescription", "Non equal traffic splitting fraction, pencentage in decimal value, under which a non shortest link should'nt be used to forward traffic."},
				{ "DeftApplyThreshold","Apply link usage threshold"},
				
				{"HybridSRPenaltyFactor","Hybrid IP/SR non SP closeness on SR domain penalty"},
				{"HybridSRPenaltyFactor.shortDescription","If 0 no penalty is applied to shortest paths that are not closed inside SR domain."},
				
				{"NumberParallelThreads","Number of threads"},
				{"NumberParallelThreads.shortDescription", "Some optimization use parallel processing. This parameter identifies the number of parallel threads to be used."},

				
				{"LFRecoveryProperty","Link Failure Recovery method"},
				{"LFRecoveryProperty.shortDescription", "The available link failure recovery methods are Topology Independent Loop Free Alternate (TI-LFA), Edge to Edge SP rerouting of traffic affected by the failure, SALP and SALP-LP  which alter both SR paths and splitting ratios."},
				{"SRNodepIterations","Number of iterations for node-p optimizations"},
				{"SRNodepIterations.shortDescription", "Number of iterations for node-p optimizations. The default value is 100."}
				
		});
	}
}
