package com.jlpbook.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PoaModelComponent {
    public enum ChemicalElement {
        CARBON("C"), COPPER("Cu"), Manganese("Mn");

        private final String name;

        ChemicalElement(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    ChemicalElement chemicalElement;
    double minimumGrade;
    double maximumGrade;
}
