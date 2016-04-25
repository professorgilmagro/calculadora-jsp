$(function(){

	var calculator = {
                CURSOR_NUMERADOR: 1,
                CURSOR_DENOMINADOR: 2,
                cursor: 1,
                new_frac : true,
                
		defaults : {
                    numerador : "#calc-numerador" ,
                    denominador : "#calc-denominador" ,
                    divisor_symbol: ".icon-divide",
                    buttons: {
                        numbers: ".calc-board button" ,
                        backspace: "#btn-backspace" ,
                        divisor: "#btn-divisor" ,
                        decimal: "#btn-decimal" ,
                        clean: "#btn-clean" ,
                        execute: "#btn-calculate" ,
                        cursor_toggle: "#btn-toogle-cursor"
                    },
                    screen: "#calc-screen",
                    warnings: "tr.visor td.content ul.warnings",
                    category_info: "tr.visor td.content div.tipo-fracao",
                    mathML: {
                        mrow : "#calc-screen math mrow",
                        mfrac:"<mfrac><mn></mn><mn></mn></mfrac>"
                    }
		} ,
		init : function( options ) {
                        this._initialize( options ) ;
			this.clean();
                        this.numbers();
                        this.backspace();
                        this.divisor();
                        this.key_events();
                        this.toggleCursor();
		} ,
                _initialize: function( options ) {
                    this.settings = $.extend( {}, this.defaults, options ) ;
                    this.$screen = $(this.settings.screen);
                    this.$entered1 = $(this.settings.entered1);
                    this.$entered2 = $(this.settings.entered2);
                    this.$warnings = $(this.settings.warnings);
                    this.$category_info = $(this.settings.category_info);
                } ,
                _updateMathContent: function(TeX) {
                    console.log(TeX);
                    
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
                        
                        var TeX = $("#mathText").val() + typed_value.toString();
                        $("#mathText").val(TeX);
                        calc._updateMathContent(TeX);
                        
//                        if ( calc.new_frac ) {
//                            $mrow.append(settings.mathML.mfrac);
//                            calc.new_frac = false;
//                            calc.cursor = calc.CURSOR_NUMERADOR;
//                        }
//                        
//                        var $mfrac = $mrow.find("mfrac").last();
//                        if( calc.cursor === calc.CURSOR_NUMERADOR ) {
//                            $mfrac.find("mn").first().append(typed_value);
//                            return ;
//                        }
//                         
//                        $mfrac.find("mn").last().append(typed_value);  
                    });
                } ,
                _fillField_value:function(typed_value){
                    if ( ! $.isNumeric(typed_value) && typed_value !== "," ) {
                        return ;
                    }
                    
                    if ( $.isNumeric( this.$entered2.val() ) || this.$screen.find(this.settings.divisor_symbol).length > 0 ){
                        if ( typed_value === "," && this.$entered2.val().indexOf( "," ) !== -1 ){
                            typed_value = '';
                        }
                        
                        this.$entered2.val(this.$entered2.val() + '' + typed_value);
                        return ;
                    }
                    
                    if ( typed_value === "," && this.$entered1.val().indexOf( "," ) !== -1 ){
                        typed_value = '';
                    }
                    
                    this.$entered1.val(this.$entered1.val() + '' + typed_value);
                } ,
                backspace: function() {
                   var settings = this.settings ;
                   var calc = this ;
                   
                   $(settings.buttons.backspace).click(function(){
                       calc._clean_info();
                       var has_divisor_symbol = calc.$screen.find(calc.settings.divisor_symbol).length > 0 ;
                                                                     
                       if ( has_divisor_symbol && calc.$entered2.val().length === 0 ) {
                           calc.$screen.find(calc.settings.divisor_symbol).remove();
                           return ;
                       }
                       
//                       var $entered = calc.$entered2.val().length > 0 ? calc.$entered2 : calc.$entered1; 
//                       var new_entered_value = $entered.val().substr(0,($entered.val().length - 1));
//                       $entered.val(new_entered_value);
//                       
//                       var screen_content = calc.$screen.html(); 
//                       var new_content = screen_content.substr(0,(screen_content.length - 1));
//                       calc.$screen.html(new_content);
                   });
                } ,
                divisor: function() {
                   var settings = this.settings ;
                   var calc = this ;
                   $(settings.buttons.divisor).click(function(){
                       if ( calc.$screen.text().length === 0 || calc.$screen.find(settings.divisor_symbol).length > 0 ) {
                           return ;
                       }
                       
                       calc._clean_info();
                       calc.$screen.append('<span class="'+settings.divisor_symbol.replace(".","")+'"></span>');
                   } ) ;
                } ,
                toggleCursor: function(){
                   var calc = this;
                   $(calc.settings.buttons.cursor_toggle).on( "click" , function (e) {
                        e.preventDefault() ;
                        $(this).find("span").removeClass("active");
                        
                        if( calc.cursor === calc.CURSOR_NUMERADOR ) {
                            $(this).find("span").last().addClass("active");
                            calc.cursor = calc.CURSOR_DENOMINADOR ;
                        }
                        else if( calc.cursor === calc.CURSOR_DENOMINADOR) {
                            calc.cursor = calc.CURSOR_NUMERADOR ;
                            $(this).find("span").first().addClass("active");
                        }
                    });
                } ,
		clean: function() {
                        var calc = this ;
			$(calc.settings.buttons.clean).on( "click" , function (e) {
                            e.preventDefault() ;
                            $( calc.settings.mathML.mrow ).empty();
                            calc.$entered1.val("");
                            calc.$entered2.val("");
                            calc._clean_info();
                            calc.cursor = calc.CURSOR_NUMERADOR;
                            $(calc.settings.buttons.cursor_toggle).find("span").removeClass("active").find("span").first("active");
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

 window.MathJax = {
    AuthorInit: function() {
      MathJax.Hub.Register.StartupHook("End", function() {
        MathJax.Hub.processSectionDelay = 0;
        var demoSource = document.getElementById('mathText');
        var demoRendering = document.getElementById('calc-screen');
        var math = MathJax.Hub.getAllJax("calc-screen")[0];
        $(demoSource).on("change" , function() {
            console.log("0i")
            MathJax.Hub.Queue(["Text", math, demoSource.value]);
        });
      });
    }
};

window.UpdateMath = function (TeX) {
    //set the MathOutput HTML
    document.getElementById("MathOutput").innerHTML = TeX;

    //reprocess the MathOutput Element
    MathJax.Hub.Queue(["Text",MathJax.Hub,TeX]);

}