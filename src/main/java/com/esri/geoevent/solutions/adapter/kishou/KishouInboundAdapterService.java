/*
 | Copyright 2014 Esri
 |
 | Licensed under the Apache License, Version 2.0 (the "License");
 | you may not use this file except in compliance with the License.
 | You may obtain a copy of the License at
 |
 |    http://www.apache.org/licenses/LICENSE-2.0
 |
 | Unless required by applicable law or agreed to in writing, software
 | distributed under the License is distributed on an "AS IS" BASIS,
 | WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 | See the License for the specific language governing permissions and
 | limitations under the License.
 */
package com.esri.geoevent.solutions.adapter.kishou;

import com.esri.ges.adapter.Adapter;
import com.esri.ges.adapter.genericJson.JsonInboundAdapterService;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.property.PropertyException;

public class KishouInboundAdapterService extends JsonInboundAdapterService {
    public KishouInboundAdapterService() throws PropertyException {
        super();
        definition = new KishouInboundAdapterDefinition();
    }

    @Override
    public Adapter createAdapter() throws ComponentException {
        return new KishouInboundAdapter(definition);
    }
}