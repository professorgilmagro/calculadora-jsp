$(function(){
    var calculator = {
            CURSOR_NUMERATOR: 1,
            CURSOR_DENOMINATOR: 2,
            FRAC_SEPARATOR: '/',
            OPERATORS : ["+", "-", "÷", "×"],
            cursor: 1,
            new_frac : true,

            defaults : {
                numerador : "#calc-numerador" ,
                denominador : "#calc-denominador" ,
                divisor_symbol: ".icon-divide",
                buttons: {
                    numbers: ".calc-board button" ,
                    backspace: "#btn-backspace" ,
                    operator: ".btn-operator" ,
                    decimal: "#btn-decimal" ,
                    clean: "#btn-clean" ,
                    execute: "#btn-calculate" ,
                    cursor_toggle: "#btn-toogle-cursor"
                },
                screen: "#calc-screen",
                mathText: "#mathText" ,
                warnings: "tr.visor td.content ul.warnings",
                category_info: "tr.visor td.content div.tipo-fracao"
            } ,
            init : function( options ) {
                this._initialize( options ) ;
                this.clean();
                this.numbers();
                this.backspace();
                this.operators();
                this.key_events();
                this.toggleCursor();
                
                
            } ,
            _initialize: function( options ) {
                this.settings = $.extend( {}, this.defaults, options ) ;
                this.$screen = $(this.settings.screen);
                this.$mathText = $(this.settings.mathText);
                this.$warnings = $(this.settings.warnings);
                this.$category_info = $(this.settings.category_info);
            } ,
            _updateMathContent: function() {
                var TeX = this.$mathText.val();
                var display = null;
                
                MathJax.Hub.Queue(function () {
                  display = MathJax.Hub.getAllJax("calc-screen")[0];
                });

                if ( display !== null ) {
                    MathJax.Hub.Queue(["Text", display, TeX]);
                }
            },
            numbers: function() {
                var settings = this.settings ;
                var calc = this ;
                $(settings.buttons.numbers).click(function(){
                    var typed_value = $(this).text();

                    if( ! $.isNumeric(typed_value)){
                        return ;
                    }

                    // remonta a nova mathText
                    var newTeX = calc._reassembleTeX(typed_value);
                    calc.$mathText.val(newTeX);
                    calc._updateMathContent();
                });
            } ,
            _reassembleTeX: function(typed_value){
                var TeX = this.$mathText.val().toString() ;
                var fracs = TeX.split(/\+|-|×|÷/);
                var fracEdit = fracs.pop().split(this.FRAC_SEPARATOR);

                // seleciona a parte a ser editada (numerador ou denominador)
                var elect = ( fracEdit.length === 2 && this.cursor === this.CURSOR_DENOMINATOR ) ? 1 : 0;
                fracEdit[elect] += typed_value.toString();

                var used = [];
                for( var i = 0; i < TeX.length; i++ ) {
                    var s = TeX.substr(i, 1) ;
                    if ( $.inArray(s, this.OPERATORS) !== -1 ){
                        used.push(s);
                    }
                }

                var newTeX = "";
                if ( used.length > 0 ) {
                   for( i = 0; i < used.length; i++ ) {
                      newTeX += fracs[i] + used[i];
                   }
                }

                newTeX += fracEdit.join(this.FRAC_SEPARATOR).toString();
                return newTeX ;
            } ,
            backspace: function() {
               var settings = this.settings ;
               var calc = this ;

               $(settings.buttons.backspace).click(function(){
                   calc._clean_info();
                   var TeX = calc.$mathText.val();
                   if ( TeX.length === 0 ) {
                       return ;
                   }
                   
                   // remove o último dígito
                   var newTeX = TeX.substring(0, TeX.length - 1);
                   
                   // verifica se o próximo dígito é o separador, caso afirmativo, removê-lo-á 
                   if( newTeX.substr(-1,1) === calc.FRAC_SEPARATOR ){
                       newTeX = newTeX.substring(0, newTeX.length - 1);
                   }
                   
                   calc.$mathText.val(newTeX);
                   calc._updateMathContent();
               });
            } ,
            operators: function() {
               var settings = this.settings ;
               var calc = this ;
               $(settings.buttons.operator).on( "click" , function(){
                    var TeX = calc.$mathText.val().toString();
                    var s = TeX.substr(-1,1);
                    if ( s === calc.FRAC_SEPARATOR || $.inArray(s, calc.OPERATORS) !== -1 ){
                        return ;
                    }
                   
                    calc.$mathText.val(TeX + $(this).text());
                    calc._updateMathContent();
                } ) ;
            } ,
            toggleCursor: function(){
               var calc = this;
               $cursor_frac = this.$screen.parents(".content").find(".cursor-frac");
               
                $(calc.settings.buttons.cursor_toggle).on( "click" , function (e) {
                    e.preventDefault() ;
                    $(this).find("span").removeClass("active");
                    $cursor_frac.find("span").removeClass("active");

                    if( calc.cursor === calc.CURSOR_NUMERATOR ) {
                        $(this).find("span").last().addClass("active");
                        $cursor_frac.find("span").last().addClass("active");
                        calc.cursor = calc.CURSOR_DENOMINATOR ;
                    }
                    else if( calc.cursor === calc.CURSOR_DENOMINATOR) {
                        calc.cursor = calc.CURSOR_NUMERATOR ;
                        $(this).find("span").first().addClass("active");
                        $cursor_frac.find("span").first().addClass("active");
                    }
                });
                
                $cursor_frac.on( "click" , function(){
                    $(calc.settings.buttons.cursor_toggle).trigger("click");
                });
            } ,
            clean: function() {
                    var calc = this ;
                    $(calc.settings.buttons.clean).on( "click" , function (e) {
                        e.preventDefault() ;
                        calc.$mathText.val("");
                        calc._updateMathContent();
                        calc._clean_info();
                        calc.cursor = calc.CURSOR_NUMERATOR;
                        $(calc.settings.buttons.cursor_toggle).find("span").removeClass("active");
                        $(calc.settings.buttons.cursor_toggle).find("span").first().addClass("active");
                    }) ;
            } ,
            _clean_info: function(){
               this.$category_info.text("");
               this.$warnings.remove(); 
            } ,
            key_events: function() {
                var calc = this ;
                $(document).on("keyup" , function(e){
                    e.preventDefault() ;

                    switch (e.keyCode) {
                        case 13:
                            $(calc.settings.buttons.execute).trigger("click");
                            break;

                        case 8:
                        case 46:
                            $(calc.settings.buttons.backspace).trigger("click");
                            break;

                        case 27:
                        case 67:
                            $(calc.settings.buttons.clean).trigger("click");
                            break;

                        case 111:
                        case 191:
                            $(calc.settings.buttons.divisor).trigger("click");
                            break;

                        case 110:
                        case 190:
                        case 108:
                        case 188:
                            $(calc.settings.buttons.decimal).trigger("click");
                            break;
                    }
                });

                $(document).on( "keypress", function(e){
                    e.preventDefault() ;

                    var charCode = String.fromCharCode(e.which);
                    if ( $.isNumeric(charCode) && parseInt(charCode) < 10 ) {
                        $(calc.settings.buttons.numbers + ":contains(" + charCode + ")" ).trigger("click");
                    }
                });

                $(document).on( "keydown", function(e){
                   if( e.keyCode === 8 ) {
                       e.preventDefault();
                   }
                } );
            }
    };

    calculator.init();
});