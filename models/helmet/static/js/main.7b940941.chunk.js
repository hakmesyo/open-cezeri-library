(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{254:function(e,t,n){e.exports=n(255)},255:function(e,t,n){"use strict";n.r(t);var r=n(3),a=n.n(r),c=n(5),o=n(9),i=n(16),u=n(19),s=n(17),f=n(15),l=n(98),d=n.n(l),v=n(231),h=n.n(v),m=n(224),p=n(145);n(281);function b(e){var t=function(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Boolean.prototype.valueOf.call(Reflect.construct(Boolean,[],function(){})),!0}catch(e){return!1}}();return function(){var n,r=Object(s.a)(e);if(t){var a=Object(s.a)(this).constructor;n=Reflect.construct(r,arguments,a)}else n=r.apply(this,arguments);return Object(u.a)(this,n)}}m.c("webgl");var y=.75;function x(){return(x=Object(f.a)(a.a.mark(function e(){var t;return a.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,Object(p.a)("https://www.turkguven.com/web_model/model.json");case 2:return t=e.sent,e.abrupt("return",t);case 4:case"end":return e.stop()}},e)}))).apply(this,arguments)}var w={1:{name:"Guvenli",id:1},2:{name:"Riskli",id:2}},g=function(e){Object(i.a)(n,e);var t=b(n);function n(){var e;Object(c.a)(this,n);for(var r=arguments.length,a=new Array(r),o=0;o<r;o++)a[o]=arguments[o];return(e=t.call.apply(t,[this].concat(a))).videoRef=d.a.createRef(),e.canvasRef=d.a.createRef(),e.detectFrame=function(t,n){m.b().startScope(),n.executeAsync(e.process_input(t)).then(function(r){e.renderPredictions(r,t),requestAnimationFrame(function(){e.detectFrame(t,n)}),m.b().endScope()})},e.renderPredictions=function(t){var n=e.canvasRef.current.getContext("2d");n.clearRect(0,0,n.canvas.width,n.canvas.height);var r="16px sans-serif";n.font=r,n.textBaseline="top";t[0].arraySync();var a=t[1].arraySync(),c=t[2].arraySync(),o=(t[3].arraySync(),t[4].arraySync()),i=(t[5].dataSync(),t[6].dataSync(),t[7].dataSync(),e.buildDetectedObjects(o,y,a,c,w));i.forEach(function(e){var t=e.bbox[0],a=e.bbox[1],c=e.bbox[2],o=e.bbox[3];n.strokeStyle="#00FFFF",n.lineWidth=4,n.strokeRect(t,a,c,o),n.fillStyle="#00FFFF";var i=n.measureText(e.label+" "+(100*e.score).toFixed(2)+"%").width,u=parseInt(r,10);n.fillRect(t,a,i+4,u+4)}),i.forEach(function(e){var t=e.bbox[0],r=e.bbox[1];n.fillStyle="#000000",n.fillText(e.label+" "+(100*e.score).toFixed(2)+"%",t,r)})},e}return Object(o.a)(n,[{key:"componentDidMount",value:function(){var e=this;if(navigator.mediaDevices&&navigator.mediaDevices.getUserMedia){var t=navigator.mediaDevices.getUserMedia({audio:!1,video:{facingMode:"user"}}).then(function(t){return window.stream=t,e.videoRef.current.srcObject=t,new Promise(function(t,n){e.videoRef.current.onloadedmetadata=function(){t()}})}),n=function(){return x.apply(this,arguments)}();Promise.all([n,t]).then(function(t){e.detectFrame(e.videoRef.current,t[0])}).catch(function(e){console.error(e)})}}},{key:"process_input",value:function(e){return m.a.fromPixels(e).toInt().transpose([0,1,2]).expandDims()}},{key:"buildDetectedObjects",value:function(e,t,n,r,a){var c=[],o=document.getElementById("frame");return e[0].forEach(function(e,i){if(e>t){var u=[],s=n[0][i][0]*o.offsetHeight,f=n[0][i][1]*o.offsetWidth,l=n[0][i][2]*o.offsetHeight,d=n[0][i][3]*o.offsetWidth;u[0]=f,u[1]=s,u[2]=d-f,u[3]=l-s,c.push({class:r[0][i],label:a[r[0][i]].name,score:e.toFixed(4),bbox:u})}}),c}},{key:"render",value:function(){return d.a.createElement("div",null,d.a.createElement("h1",null,"Koruyucu Ba\u015fl\u0131k Alg\u0131lama"),d.a.createElement("video",{style:{height:"600px",width:"500px"},className:"size",autoPlay:!0,playsInline:!0,muted:!0,ref:this.videoRef,width:"600",height:"500",id:"frame"}),d.a.createElement("canvas",{className:"size",ref:this.canvasRef,width:"600",height:"500"}))}}]),n}(d.a.Component),R=document.getElementById("root");h.a.render(d.a.createElement(g,null),R)},266:function(e,t){},267:function(e,t){},275:function(e,t){},278:function(e,t){},279:function(e,t){},280:function(e,t){},281:function(e,t,n){}},[[254,2,1]]]);
//# sourceMappingURL=main.7b940941.chunk.js.map