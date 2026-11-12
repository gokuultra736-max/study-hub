package com.example.data.repository

import com.example.data.model.FlashcardItem
import com.example.data.model.NcertBook
import com.example.data.model.NcertChapter
import com.example.data.model.QuizQuestion

object NcertDataProvider {

    val supportedGrades = (5..12).toList()

    fun getBooksForGrade(grade: Int): List<NcertBook> {
        return allBooks.filter { it.grade == grade }
    }

    fun getBookById(bookId: String): NcertBook? {
        return allBooks.find { it.id == bookId }
    }

    fun getChapter(bookId: String, chapterNumber: Int): NcertChapter? {
        return allBooks.find { it.id == bookId }?.chapters?.find { it.chapterNumber == chapterNumber }
    }

    fun getPreloadedFlashcards(grade: Int, subject: String? = null): List<FlashcardItem> {
        val filtered = allFlashcards.filter { it.grade == grade }
        return if (subject != null && subject != "All") {
            filtered.filter { it.subject.equals(subject, ignoreCase = true) }
        } else {
            filtered
        }
    }

    fun getQuizzesForGrade(grade: Int, subject: String? = null): List<QuizQuestion> {
        val filtered = allQuizQuestions.filter { it.grade == grade }
        return if (subject != null && subject != "All") {
            filtered.filter { it.subject.equals(subject, ignoreCase = true) }
        } else {
            filtered
        }
    }

    // Catalog of NCERT Books (Classes 5 to 12) - 2026 Syllabus
    private val allBooks = listOf(
        // ================= CLASS 10 =================
        NcertBook(
            id = "c10_science",
            title = "Science (Class 10)",
            grade = 10,
            subject = "Science",
            edition = "2026 Rationalised Edition",
            description = "Standard textbook for Class 10 covering Chemical Reactions, Life Processes, Light, Electricity, Magnetic Effects, and Our Environment.",
            totalChapters = 13,
            chapters = listOf(
                NcertChapter(
                    id = "c10_sci_ch1",
                    bookId = "c10_science",
                    chapterNumber = 1,
                    title = "Chemical Reactions and Equations",
                    overview = "Learn chemical equations, balancing techniques, types of chemical reactions (combination, decomposition, displacement, double displacement, redox), and everyday effects of oxidation (corrosion and rancidity).",
                    keyPoints = listOf(
                        "A complete chemical equation represents the reactants, products, and their physical states symbolically.",
                        "Law of conservation of mass: Matter can neither be created nor destroyed in a chemical reaction.",
                        "Exothermic reactions release energy; endothermic reactions absorb heat/energy.",
                        "Corrosion: Oxidation of metals in the presence of moisture and air (e.g. rusting of iron: 4Fe + 3O₂ + 2xH₂O → 2Fe₂O₃·xH₂O).",
                        "Rancidity: Oxidation of fats and oils leading to foul smell and taste; prevented using antioxidants or nitrogen flush."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Combination" to "A + B → AB (e.g. CaO + H₂O → Ca(OH)₂ + Heat)",
                        "Decomposition" to "AB → A + B (e.g. 2FeSO₄ → Fe₂O₃ + SO₂ + SO₃)",
                        "Displacement" to "A + BC → AC + B (e.g. Fe + CuSO₄ → FeSO₄ + Cu)",
                        "Redox" to "Simultaneous reduction (gain of electrons/H, loss of O) and oxidation (loss of electrons/H, gain of O)"
                    ),
                    practiceQuestions = listOf(
                        "Why should a magnesium ribbon be cleaned before burning in air?",
                        "What is a balanced chemical equation? Why should chemical equations be balanced?",
                        "Why is respiration considered an exothermic reaction?",
                        "Identify the substances oxidized and reduced in: 4Na(s) + O₂(g) → 2Na₂O(s)"
                    ),
                    fullReadingContent = """
                        # Chapter 1: Chemical Reactions and Equations (NCERT 2026)

                        ## 1.1 Chemical Reaction
                        Whenever a chemical change occurs, we can say that a chemical reaction has taken place.
                        Observations determining a chemical reaction:
                        - Change in state
                        - Change in colour
                        - Evolution of a gas
                        - Change in temperature

                        ## 1.2 Balanced Chemical Equations
                        According to the Law of Conservation of Mass:
                        Total mass of elements present in products = Total mass of elements present in reactants.
                        Number of atoms of each element remains identical before and after a chemical reaction.
                        Example:
                        Zn + H₂SO₄ → ZnSO₄ + H₂↑

                        ## 1.3 Types of Chemical Reactions
                        1. Combination Reaction: Two or more reactants combine to form a single product.
                        2. Decomposition Reaction: A single reactant breaks down to yield simpler products (Thermal, Electrolytic, Photochemical).
                        3. Displacement Reaction: More reactive element displaces a less reactive element.
                        4. Double Displacement Reaction: Exchange of ions between reactants forming a precipitate.
                        5. Oxidation & Reduction: Oxidation is gain of oxygen or loss of hydrogen. Reduction is loss of oxygen or gain of hydrogen.

                        ## 1.4 Corrosion & Rancidity
                        - Corrosion causes enormous damage to car bodies, bridges, iron railings, ships.
                        - Methods of prevention: Galvanization, painting, greasing, alloying.
                        - Rancidity prevention: Air-tight containers, flush bags with Nitrogen gas.
                    """.trimIndent()
                ),
                NcertChapter(
                    id = "c10_sci_ch2",
                    bookId = "c10_science",
                    chapterNumber = 2,
                    title = "Acids, Bases and Salts",
                    overview = "Understanding indicators, neutralization, pH scale, properties of acids and bases, and important salts like Bleaching Powder, Baking Soda, Washing Soda, and Plaster of Paris.",
                    keyPoints = listOf(
                        "Acids taste sour, turn blue litmus red, and release H⁺ (or H₃O⁺) ions in aqueous solution.",
                        "Bases taste bitter, turn red litmus blue, feel soapy, and release OH⁻ ions.",
                        "pH scale ranges from 0 (strongly acidic) to 14 (strongly alkaline); pH 7 is neutral.",
                        "Plaster of Paris (CaSO₄·½H₂O) sets into hard gypsum (CaSO₄·2H₂O) upon mixing with water."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Neutralization" to "Acid + Base → Salt + Water (HCl + NaOH → NaCl + H₂O)",
                        "Baking Soda" to "NaHCO₃ (Sodium hydrogen carbonate)",
                        "Washing Soda" to "Na₂CO₃·10H₂O (Sodium carbonate decahydrate)",
                        "Plaster of Paris" to "CaSO₄·½H₂O"
                    ),
                    practiceQuestions = listOf(
                        "Why does an aqueous solution of an acid conduct electricity?",
                        "What is the common name of compound CaOCl₂?",
                        "Name the sodium compound used for softening hard water."
                    ),
                    fullReadingContent = """
                        # Chapter 2: Acids, Bases and Salts

                        ## 2.1 Chemical Properties
                        Acids react with metals to produce salt and hydrogen gas:
                        Acid + Metal → Salt + Hydrogen gas

                        ## 2.2 Importance of pH in Everyday Life
                        - Plants and animals are pH sensitive: Human body works within pH range 7.0 to 7.8.
                        - Acid rain: When pH of rain water is less than 5.6.
                        - Tooth decay begins when the pH of the mouth is lower than 5.5.

                        ## 2.3 Chemicals from Common Salt
                        - Sodium Hydroxide (Chlor-alkali process)
                        - Bleaching Powder: Ca(OH)₂ + Cl₂ → CaOCl₂ + H₂O
                        - Baking Soda: NaCl + H₂O + CO₂ + NH₃ → NH₄Cl + NaHCO₃
                        - Plaster of Paris: On heating gypsum at 373 K, it loses water molecules.
                    """.trimIndent()
                ),
                NcertChapter(
                    id = "c10_sci_ch6",
                    bookId = "c10_science",
                    chapterNumber = 6,
                    title = "Life Processes",
                    overview = "Core biological processes essential for maintenance of life: Nutrition (autotrophic & heterotrophic), Respiration, Transportation in humans and plants, and Excretion.",
                    keyPoints = listOf(
                        "Autotrophic nutrition: Photosynthesis converts CO₂ and H₂O into glucose using chlorophyll and sunlight.",
                        "Aerobic respiration occurs in mitochondria producing 36-38 ATP; anaerobic occurs in yeast/muscles.",
                        "Human circulatory system includes heart (4 chambers), blood (RBCs, WBCs, platelets, plasma), and vessels.",
                        "Nephron is the basic structural and functional filtration unit of kidneys."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Photosynthesis" to "6CO₂ + 12H₂O → C₆H₁₂O₆ + 6O₂ + 6H₂O (in presence of sunlight & chlorophyll)",
                        "ATP" to "Adenosine triphosphate - the energy currency of the cell"
                    ),
                    practiceQuestions = listOf(
                        "What are the differences between autotrophic and heterotrophic nutrition?",
                        "How are the alveoli designed to maximize exchange of gases?",
                        "Describe the structure and functioning of nephrons."
                    ),
                    fullReadingContent = """
                        # Chapter 6: Life Processes

                        ## Nutrition
                        Autotrophs utilize simple inorganic substances and synthesize their food.
                        Heterotrophs depend directly or indirectly on autotrophs.

                        ## Respiration
                        Breakdown of glucose:
                        - In Cytoplasm: Glucose (6-C) → Pyruvate (3-C) + Energy
                        - In Yeast (Absence of O₂): Ethanol + CO₂ + Energy
                        - In Muscle Cells (Lack of O₂): Lactic acid + Energy
                        - In Mitochondria (Presence of O₂): CO₂ + H₂O + Energy

                        ## Excretion in Humans
                        Kidneys filter nitrogenous waste (urea/uric acid) through nephrons.
                    """.trimIndent()
                ),
                NcertChapter(
                    id = "c10_sci_ch9",
                    bookId = "c10_science",
                    chapterNumber = 9,
                    title = "Light - Reflection and Refraction",
                    overview = "Spherical mirrors (concave and convex), mirror formula, magnification, refraction of light, Snell's law, refractive index, and lens formula.",
                    keyPoints = listOf(
                        "Laws of reflection: Angle of incidence = Angle of reflection; incident ray, reflected ray, normal lie in same plane.",
                        "Mirror formula: 1/f = 1/v + 1/u.",
                        "Lens formula: 1/f = 1/v - 1/u.",
                        "Power of a lens: P = 1/f (in meters), unit is Dioptre (D)."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Mirror formula" to "1/f = 1/v + 1/u",
                        "Lens formula" to "1/f = 1/v - 1/u",
                        "Snell's Law" to "sin i / sin r = constant (n₂₁)",
                        "Power of Lens" to "P = 1/f (m) (Dioptre)"
                    ),
                    practiceQuestions = listOf(
                        "Find the focal length of a convex mirror whose radius of curvature is 32 cm.",
                        "A concave mirror produces three times magnified real image of an object placed at 10 cm in front of it. Where is the image located?",
                        "Define 1 dioptre of power of a lens."
                    ),
                    fullReadingContent = """
                        # Chapter 9: Light - Reflection and Refraction

                        ## 9.1 Reflection of Light
                        Concave mirror converges parallel light rays at the principal focus.
                        Convex mirror diverges light rays; used as rear-view mirrors in vehicles because of wide field of view.

                        ## 9.2 Refraction of Light
                        Bending of light as it travels from one transparent medium to another.
                        Absolute refractive index: n = c / v (speed of light in vacuum / speed in medium).
                    """.trimIndent()
                )
            )
        ),
        NcertBook(
            id = "c10_math",
            title = "Mathematics (Class 10)",
            grade = 10,
            subject = "Mathematics",
            edition = "2026 Rationalised Edition",
            description = "Real Numbers, Polynomials, Linear Equations, Quadratic Equations, Arithmetic Progressions, Triangles, Coordinate Geometry, Trigonometry, Statistics & Probability.",
            totalChapters = 14,
            chapters = listOf(
                NcertChapter(
                    id = "c10_math_ch1",
                    bookId = "c10_math",
                    chapterNumber = 1,
                    title = "Real Numbers",
                    overview = "Fundamental Theorem of Arithmetic, proving irrationality of √2, √3, √5, prime factorization, and HCF × LCM relationship.",
                    keyPoints = listOf(
                        "Fundamental Theorem of Arithmetic: Every composite number can be expressed as a product of primes uniquely.",
                        "For any two positive integers a and b, HCF(a, b) × LCM(a, b) = a × b.",
                        "A number is irrational if it cannot be written in the form p/q where p, q are integers and q ≠ 0."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "HCF × LCM" to "HCF(a, b) × LCM(a, b) = a × b",
                        "Irrationality Proof" to "Assumed rational p/q coprime, squarings lead to contradiction"
                    ),
                    practiceQuestions = listOf(
                        "Express 140 as a product of its prime factors.",
                        "Given that HCF(306, 657) = 9, find LCM(306, 657).",
                        "Prove that √5 is irrational."
                    ),
                    fullReadingContent = """
                        # Chapter 1: Real Numbers

                        ## Fundamental Theorem of Arithmetic
                        Every composite number can be uniquely factored as a product of prime powers.
                        Example: 32760 = 2³ × 3² × 5 × 7 × 13.

                        ## Re-visiting Irrational Numbers
                        Theorem: If p is a prime number and p divides a², then p divides a, where a is a positive integer.
                    """.trimIndent()
                ),
                NcertChapter(
                    id = "c10_math_ch4",
                    bookId = "c10_math",
                    chapterNumber = 4,
                    title = "Quadratic Equations",
                    overview = "Standard form ax² + bx + c = 0, solution by factorization, quadratic formula, and discriminant nature of roots.",
                    keyPoints = listOf(
                        "Standard form: ax² + bx + c = 0 (a ≠ 0).",
                        "Quadratic formula: x = (-b ± √(b² - 4ac)) / (2a).",
                        "Discriminant D = b² - 4ac: D > 0 (two distinct real roots), D = 0 (two equal real roots), D < 0 (no real roots)."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Quadratic Formula" to "x = (-b ± √D) / (2a)",
                        "Discriminant" to "D = b² - 4ac"
                    ),
                    practiceQuestions = listOf(
                        "Find the roots of 2x² - 5x + 3 = 0 by factorization.",
                        "Find the discriminant of 2x² - 4x + 3 = 0 and discuss nature of roots.",
                        "Find two consecutive positive integers, sum of whose squares is 365."
                    ),
                    fullReadingContent = """
                        # Chapter 4: Quadratic Equations

                        A second-degree polynomial equation.
                        Nature of Roots:
                        - Two distinct real roots if b² - 4ac > 0
                        - Two equal real roots if b² - 4ac = 0
                        - No real roots if b² - 4ac < 0
                    """.trimIndent()
                ),
                NcertChapter(
                    id = "c10_math_ch8",
                    bookId = "c10_math",
                    chapterNumber = 8,
                    title = "Introduction to Trigonometry",
                    overview = "Trigonometric ratios (sin, cos, tan, cot, sec, cosec), values for standard angles 0°, 30°, 45°, 60°, 90°, and trigonometric identities.",
                    keyPoints = listOf(
                        "sin θ = Opp/Hyp, cos θ = Adj/Hyp, tan θ = Opp/Adj.",
                        "Identity 1: sin² θ + cos² θ = 1.",
                        "Identity 2: 1 + tan² θ = sec² θ.",
                        "Identity 3: 1 + cot² θ = cosec² θ."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Pythagorean Identity" to "sin² θ + cos² θ = 1",
                        "Tan Identity" to "1 + tan² θ = sec² θ",
                        "Cot Identity" to "1 + cot² θ = cosec² θ"
                    ),
                    practiceQuestions = listOf(
                        "If sin A = 3/4, calculate cos A and tan A.",
                        "Evaluate: (2 tan 30°) / (1 + tan² 30°).",
                        "Prove that (sin θ - 2 sin³ θ) / (2 cos³ θ - cos θ) = tan θ."
                    ),
                    fullReadingContent = """
                        # Chapter 8: Introduction to Trigonometry

                        Values of Trigonometric Ratios:
                        - sin 0° = 0, sin 30° = 1/2, sin 45° = 1/√2, sin 60° = √3/2, sin 90° = 1.
                        - cos 0° = 1, cos 30° = √3/2, cos 45° = 1/√2, cos 60° = 1/2, cos 90° = 0.
                        - tan 45° = 1.
                    """.trimIndent()
                )
            )
        ),
        // ================= CLASS 9 =================
        NcertBook(
            id = "c9_science",
            title = "Science (Class 9)",
            grade = 9,
            subject = "Science",
            edition = "2026 Rationalised Edition",
            description = "Matter in Our Surroundings, Atoms & Molecules, Structure of Atom, Fundamental Unit of Life, Motion, Force and Laws of Motion, Gravitation, Work & Energy.",
            totalChapters = 12,
            chapters = listOf(
                NcertChapter(
                    id = "c9_sci_ch8",
                    bookId = "c9_science",
                    chapterNumber = 8,
                    title = "Motion",
                    overview = "Distance and displacement, uniform and non-uniform motion, speed and velocity, acceleration, and equations of motion.",
                    keyPoints = listOf(
                        "Displacement is the shortest distance between initial and final position.",
                        "Velocity is speed with direction (v = s/t).",
                        "Acceleration a = (v - u) / t.",
                        "Equations of motion: v = u + at; s = ut + ½at²; v² = u² + 2as."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "First Equation" to "v = u + at",
                        "Second Equation" to "s = ut + ½at²",
                        "Third Equation" to "v² = u² + 2as"
                    ),
                    practiceQuestions = listOf(
                        "Distinguish between speed and velocity.",
                        "A bus starting from rest moves with uniform acceleration of 0.1 m/s² for 2 minutes. Find speed acquired and distance traveled."
                    ),
                    fullReadingContent = "Chapter 8: Motion - Equations of uniform acceleration and graphical representation."
                )
            )
        ),
        NcertBook(
            id = "c9_math",
            title = "Mathematics (Class 9)",
            grade = 9,
            subject = "Mathematics",
            edition = "2026 Rationalised Edition",
            description = "Number Systems, Polynomials, Coordinate Geometry, Linear Equations in Two Variables, Lines & Angles, Triangles, Quadrilaterals, Circles, Heron's Formula, Surface Areas & Volumes.",
            totalChapters = 12,
            chapters = listOf(
                NcertChapter(
                    id = "c9_math_ch1",
                    bookId = "c9_math",
                    chapterNumber = 1,
                    title = "Number Systems",
                    overview = "Rational numbers, representing real numbers on number line, laws of exponents, rationalizing denominators.",
                    keyPoints = listOf(
                        "Rational numbers can be expressed as p/q (q ≠ 0).",
                        "Decimal expansion of rational number is either terminating or non-terminating recurring.",
                        "Rationalizing factor for 1/(a + √b) is (a - √b)."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Laws of exponents" to "aᵐ × aⁿ = aᵐ⁺ⁿ; (aᵐ)ⁿ = aᵐⁿ"
                    ),
                    practiceQuestions = listOf(
                        "Find 5 rational numbers between 3/5 and 4/5.",
                        "Rationalize the denominator of 1/(7 + 3√2)."
                    ),
                    fullReadingContent = "Chapter 1: Number Systems - Detailed breakdown of real numbers and representations."
                )
            )
        ),
        // ================= CLASS 12 =================
        NcertBook(
            id = "c12_physics",
            title = "Physics Part 1 & 2 (Class 12)",
            grade = 12,
            subject = "Physics",
            edition = "2026 Rationalised Edition",
            description = "Electric Charges and Fields, Electrostatic Potential, Current Electricity, Moving Charges and Magnetism, Optics, Semiconductor Electronics.",
            totalChapters = 14,
            chapters = listOf(
                NcertChapter(
                    id = "c12_phy_ch1",
                    bookId = "c12_physics",
                    chapterNumber = 1,
                    title = "Electric Charges and Fields",
                    overview = "Coulomb's Law, Electric Field, Electric Dipole, Electric Flux, and Gauss's Law with applications.",
                    keyPoints = listOf(
                        "Coulomb's Law: F = (1 / 4πε₀) × (|q₁q₂| / r²).",
                        "Electric field due to point charge: E = q / (4πε₀ r²).",
                        "Gauss's Law: Total flux Φ = ∮ E·dA = q_enclosed / ε₀."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Coulomb's Law" to "F = (1/4πε₀) · (q₁q₂ / r²)",
                        "Gauss's Theorem" to "Φ = ∮ E · dA = q_in / ε₀"
                    ),
                    practiceQuestions = listOf(
                        "State Coulomb's law in vector form.",
                        "Derive an expression for electric field at an axial point of a dipole."
                    ),
                    fullReadingContent = "Chapter 1: Electric Charges and Fields - Comprehensive theory, derivations, and numericals."
                )
            )
        ),
        NcertBook(
            id = "c12_math",
            title = "Mathematics (Class 12)",
            grade = 12,
            subject = "Mathematics",
            edition = "2026 Rationalised Edition",
            description = "Relations and Functions, Inverse Trigonometric Functions, Matrices, Determinants, Continuity and Differentiability, Integrals, Differential Equations, Vectors.",
            totalChapters = 13,
            chapters = listOf(
                NcertChapter(
                    id = "c12_math_ch3",
                    bookId = "c12_math",
                    chapterNumber = 3,
                    title = "Matrices and Determinants",
                    overview = "Types of matrices, matrix addition & multiplication, inverse of a matrix, determinants and Cramer's rule.",
                    keyPoints = listOf(
                        "Matrix multiplication is non-commutative in general (AB ≠ BA).",
                        "A square matrix has an inverse if and only if |A| ≠ 0.",
                        "A⁻¹ = adj(A) / |A|."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Matrix Inverse" to "A⁻¹ = (1 / |A|) · adj(A)"
                    ),
                    practiceQuestions = listOf(
                        "If A is a square matrix of order 3 with |A| = 4, find |adj A|.",
                        "Solve the system of equations using matrix method: 2x + 5y = 1, 3x + 2y = 7."
                    ),
                    fullReadingContent = "Chapter 3: Matrices and Determinants - Full formulas, theorems, and row transformations."
                )
            )
        ),
        // ================= CLASS 11 =================
        NcertBook(
            id = "c11_chem",
            title = "Chemistry (Class 11)",
            grade = 11,
            subject = "Chemistry",
            edition = "2026 Rationalised Edition",
            description = "Some Basic Concepts of Chemistry, Structure of Atom, Classification of Elements, Chemical Bonding, Thermodynamics, Equilibrium, Organic Chemistry.",
            totalChapters = 9,
            chapters = listOf(
                NcertChapter(
                    id = "c11_chem_ch1",
                    bookId = "c11_chem",
                    chapterNumber = 1,
                    title = "Some Basic Concepts of Chemistry",
                    overview = "Mole concept, molar mass, stoichiometry, empirical formula, molarity, and molality.",
                    keyPoints = listOf(
                        "1 mole = 6.022 × 10²³ particles (Avogadro's number).",
                        "Molarity (M) = Moles of solute / Volume of solution in liters.",
                        "Molality (m) = Moles of solute / Mass of solvent in kg."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Mole Concept" to "n = mass / molar mass",
                        "Molarity" to "M = n / V (L)"
                    ),
                    practiceQuestions = listOf(
                        "Calculate the molecular mass of glucose (C₆H₁₂O₆).",
                        "Calculate the molarity of NaOH in the solution prepared by dissolving 4 g in enough water to form 250 mL of solution."
                    ),
                    fullReadingContent = "Chapter 1: Basic Concepts of Chemistry - Mole calculations, limiting reagent, and stoichiometric problems."
                )
            )
        ),
        // ================= CLASS 8 =================
        NcertBook(
            id = "c8_science",
            title = "Science (Class 8)",
            grade = 8,
            subject = "Science",
            edition = "2026 Rationalised Edition",
            description = "Crop Production, Microorganisms, Coal and Petroleum, Combustion and Flame, Cell Structure, Reproduction in Animals, Force & Pressure, Sound, Light.",
            totalChapters = 13,
            chapters = listOf(
                NcertChapter(
                    id = "c8_sci_ch8",
                    bookId = "c8_science",
                    chapterNumber = 8,
                    title = "Cell - Structure and Functions",
                    overview = "Discovery of cell by Robert Hooke, unicellular vs multicellular organisms, cell membrane, cytoplasm, nucleus, and organelles.",
                    keyPoints = listOf(
                        "Cell is the basic structural and functional unit of life.",
                        "Plant cells have an outer rigid cell wall and chloroplasts; animal cells do not.",
                        "Nucleus contains chromosomes carrying genes that transfer inheritance."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Cell theory" to "All living organisms are made up of cells"
                    ),
                    practiceQuestions = listOf(
                        "Name three parts of a cell.",
                        "Differentiate between plant cell and animal cell."
                    ),
                    fullReadingContent = "Chapter 8: Cell Structure & Functions - Detailed diagrams, organelles, and roles."
                )
            )
        ),
        // ================= CLASS 7 =================
        NcertBook(
            id = "c7_math",
            title = "Mathematics (Class 7)",
            grade = 7,
            subject = "Mathematics",
            edition = "2026 Edition",
            description = "Integers, Fractions & Decimals, Data Handling, Simple Equations, Lines and Angles, Triangles, Comparing Quantities, Algebraic Expressions, Exponents.",
            totalChapters = 13,
            chapters = listOf(
                NcertChapter(
                    id = "c7_math_ch1",
                    bookId = "c7_math",
                    chapterNumber = 1,
                    title = "Integers",
                    overview = "Properties of addition, subtraction, multiplication, and division of positive and negative integers.",
                    keyPoints = listOf(
                        "Closure property: Sum and difference of two integers is always an integer.",
                        "Multiplication rule: Product of two negative integers is positive.",
                        "Division by zero is meaningless/undefined."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Distributive Law" to "a × (b + c) = (a × b) + (a × c)"
                    ),
                    practiceQuestions = listOf(
                        "Evaluate: (-30) ÷ 10",
                        "Verify: a - (-b) = a + b for a = 21, b = 18"
                    ),
                    fullReadingContent = "Chapter 1: Integers - Rules of signs, associative & distributive laws."
                )
            )
        ),
        // ================= CLASS 6 (Latest 2024-2026 NCF) =================
        NcertBook(
            id = "c6_curiosity",
            title = "Curiosity: Science (Class 6)",
            grade = 6,
            subject = "Science",
            edition = "2026 NEP Edition",
            description = "The new NCERT 2024-2026 textbook for Class 6: Exploring the Natural World, Food, Materials, Living Creatures, Measurement & Motion, Light, Water and Air.",
            totalChapters = 12,
            chapters = listOf(
                NcertChapter(
                    id = "c6_sci_ch1",
                    bookId = "c6_curiosity",
                    chapterNumber = 1,
                    title = "The Wonderful World of Science",
                    overview = "Introduction to scientific thinking, inquiry, observations, hypothesis testing, and the scientific method in daily life.",
                    keyPoints = listOf(
                        "Science is observing and asking questions about nature.",
                        "Scientists perform experiments and collect data to verify hypotheses.",
                        "Curiosity is the engine of all scientific discoveries."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Scientific Method" to "Question → Hypothesis → Experiment → Conclusion"
                    ),
                    practiceQuestions = listOf(
                        "What is observation in science?",
                        "Give two examples of how science is used in your home."
                    ),
                    fullReadingContent = "Chapter 1: The Wonderful World of Science - New 2026 NEP experiential learning framework."
                )
            )
        ),
        // ================= CLASS 5 =================
        NcertBook(
            id = "c5_math_magic",
            title = "Math-Magic (Class 5)",
            grade = 5,
            subject = "Mathematics",
            edition = "2026 Edition",
            description = "The Fish Tale, Shapes and Angles, How Many Squares?, Parts and Wholes, Does it Look the Same?, Be My Multiple, I'll be Your Factor, Boxes and Sketches.",
            totalChapters = 14,
            chapters = listOf(
                NcertChapter(
                    id = "c5_math_ch1",
                    bookId = "c5_math_magic",
                    chapterNumber = 1,
                    title = "The Fish Tale",
                    overview = "Fun word problems involving large numbers (lakhs, crores), speed, boat travel, distance calculations, and fish market economics.",
                    keyPoints = listOf(
                        "One Lakh = 1,00,000 (100 thousands).",
                        "One Crore = 1,00,00,000 (100 lakhs).",
                        "Speed = Distance / Time."
                    ),
                    importantFormulasOrDefinitions = listOf(
                        "Distance" to "Speed × Time"
                    ),
                    practiceQuestions = listOf(
                        "If a log boat goes 4 km in 1 hour, how long will it take to go 10 km?",
                        "Write one lakh in numerals."
                    ),
                    fullReadingContent = "Chapter 1: The Fish Tale - Experiential math using Indian coastal storytelling."
                )
            )
        )
    )

    // Preloaded Flashcards (2026 NCERT Syllabus)
    private val allFlashcards = listOf(
        FlashcardItem(
            id = 101,
            deckName = "Class 10 Science",
            grade = 10,
            subject = "Science",
            question = "What is the formula of Plaster of Paris and Gypsum?",
            answer = "Plaster of Paris: CaSO₄·½H₂O (Calcium sulphate hemihydrate).\nGypsum: CaSO₄·2H₂O.",
            hint = "Half molecule of water vs two molecules."
        ),
        FlashcardItem(
            id = 102,
            deckName = "Class 10 Science",
            grade = 10,
            subject = "Science",
            question = "What is Snell's Law of refraction?",
            answer = "The ratio of the sine of angle of incidence to the sine of angle of refraction is a constant for a given pair of media: sin i / sin r = constant = n₂₁.",
            hint = "Relates sin i and sin r."
        ),
        FlashcardItem(
            id = 103,
            deckName = "Class 10 Math",
            grade = 10,
            subject = "Mathematics",
            question = "What is the quadratic formula for ax² + bx + c = 0?",
            answer = "x = (-b ± √(b² - 4ac)) / (2a).\nThe term (b² - 4ac) is the Discriminant D.",
            hint = "Contains -b plus/minus square root of D."
        ),
        FlashcardItem(
            id = 104,
            deckName = "Class 10 Math",
            grade = 10,
            subject = "Mathematics",
            question = "What are the three main Pythagorean trigonometric identities?",
            answer = "1. sin² θ + cos² θ = 1\n2. 1 + tan² θ = sec² θ\n3. 1 + cot² θ = cosec² θ",
            hint = "sin & cos, tan & sec, cot & cosec."
        ),
        FlashcardItem(
            id = 105,
            deckName = "Class 9 Science",
            grade = 9,
            subject = "Science",
            question = "State Newton's Second Law of Motion.",
            answer = "The rate of change of momentum of an object is directly proportional to the applied unbalanced force in the direction of force: F = ma.",
            hint = "F equals m times a."
        ),
        FlashcardItem(
            id = 106,
            deckName = "Class 12 Physics",
            grade = 12,
            subject = "Physics",
            question = "State Gauss's Law in Electrostatics.",
            answer = "The total electric flux through a closed surface S is equal to 1/ε₀ times the total charge enclosed by the surface: ∮ E·dA = q_enclosed / ε₀.",
            hint = "Flux = charge enclosed / epsilon zero."
        ),
        FlashcardItem(
            id = 107,
            deckName = "Class 8 Science",
            grade = 8,
            subject = "Science",
            question = "Which cell organelle is known as the 'Powerhouse of the Cell'?",
            answer = "Mitochondria - because it produces energy in the form of ATP molecules through cellular respiration.",
            hint = "Starts with M."
        ),
        FlashcardItem(
            id = 108,
            deckName = "Class 6 Science",
            grade = 6,
            subject = "Science",
            question = "What is the boiling point and freezing point of pure water in Celsius?",
            answer = "Boiling point: 100°C (at standard atmospheric pressure).\nFreezing point: 0°C.",
            hint = "Zero and hundred."
        )
    )

    // Preloaded Quizzes (2026 NCERT questions)
    private val allQuizQuestions = listOf(
        QuizQuestion(
            id = 1,
            grade = 10,
            subject = "Science",
            chapter = "Chemical Reactions & Equations",
            question = "What type of reaction is the heating of limestone (CaCO₃ → CaO + CO₂)?",
            options = listOf("Combination Reaction", "Thermal Decomposition", "Displacement Reaction", "Redox Reaction"),
            correctIndex = 1,
            explanation = "Heating limestone breaks a single compound into calcium oxide and carbon dioxide using heat energy, which is a thermal decomposition reaction."
        ),
        QuizQuestion(
            id = 2,
            grade = 10,
            subject = "Science",
            chapter = "Acids, Bases & Salts",
            question = "What is the pH value of a neutral aqueous solution at 25°C?",
            options = listOf("0", "5.6", "7", "14"),
            correctIndex = 2,
            explanation = "A neutral solution like pure water has equal concentration of H⁺ and OH⁻ ions, corresponding to pH 7 on the scale."
        ),
        QuizQuestion(
            id = 3,
            grade = 10,
            subject = "Mathematics",
            chapter = "Quadratic Equations",
            question = "If the discriminant b² - 4ac = 0 for ax² + bx + c = 0, what is the nature of the roots?",
            options = listOf("No real roots", "Two distinct real roots", "Two equal real roots", "Imaginary roots only"),
            correctIndex = 2,
            explanation = "When D = 0, the quadratic formula yields x = -b / (2a), resulting in two equal (coincident) real roots."
        ),
        QuizQuestion(
            id = 4,
            grade = 10,
            subject = "Mathematics",
            chapter = "Trigonometry",
            question = "What is the value of sin 30° + cos 60°?",
            options = listOf("1/2", "1", "√3/2", "0"),
            correctIndex = 1,
            explanation = "sin 30° = 1/2 and cos 60° = 1/2. Therefore, 1/2 + 1/2 = 1."
        ),
        QuizQuestion(
            id = 5,
            grade = 9,
            subject = "Science",
            chapter = "Motion",
            question = "What does the slope of a distance-time graph represent?",
            options = listOf("Acceleration", "Speed", "Force", "Displacement"),
            correctIndex = 1,
            explanation = "Slope of a distance-time graph is change in distance divided by change in time (Δs/Δt), which is speed."
        ),
        QuizQuestion(
            id = 6,
            grade = 12,
            subject = "Physics",
            chapter = "Electrostatics",
            question = "What is the SI unit of Electric Flux?",
            options = listOf("N/C", "N·m²/C", "V/m", "C/m²"),
            correctIndex = 1,
            explanation = "Flux Φ = E · A. The unit of E is N/C and area is m², giving N·m²/C (or Volt·meter)."
        ),
        QuizQuestion(
            id = 7,
            grade = 8,
            subject = "Science",
            chapter = "Cell Structure",
            question = "Which organelle contains chlorophyll for photosynthesis in plant cells?",
            options = listOf("Mitochondria", "Ribosome", "Chloroplast", "Golgi apparatus"),
            correctIndex = 2,
            explanation = "Chloroplasts contain chlorophyll that traps sunlight for photosynthesis."
        ),
        QuizQuestion(
            id = 8,
            grade = 6,
            subject = "Science",
            chapter = "Curiosity 2026",
            question = "Which of the following is an abiotic component of our environment?",
            options = listOf("Plants", "Bacteria", "Sunlight & Soil", "Insects"),
            correctIndex = 2,
            explanation = "Abiotic factors are non-living components of an ecosystem like water, soil, air, and sunlight."
        )
    )
}
