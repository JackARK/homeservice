import{k as H,l as x,m as y,p as g,C as f,q as T,J as K,D as w,d as k,h as i,Q as U,t as W,v as R,aa as J,y as B,U as Q,aT as X,aq as Y,E as G,b0 as Z,aU as ee,b1 as oe,b2 as ne,aj as re,z as $,r as j,b3 as te,am as v,aN as se,aw as ie,aD as le,R as ae,b4 as ce,a2 as de}from"./index-B19EQUzC.js";import{u as ue}from"./use-compitable-BU_QcteX.js";const he={iconMargin:"11px 8px 0 12px",iconMarginRtl:"11px 12px 0 8px",iconSize:"24px",closeIconSize:"16px",closeSize:"20px",closeMargin:"13px 14px 0 0",closeMarginRtl:"13px 0 0 14px",padding:"13px"};function ge(e){const{lineHeight:o,borderRadius:r,fontWeightStrong:s,baseColor:l,dividerColor:a,actionColor:p,textColor1:c,textColor2:n,closeColorHover:d,closeColorPressed:h,closeIconColor:m,closeIconColorHover:C,closeIconColorPressed:u,infoColor:t,successColor:z,warningColor:I,errorColor:S,fontSize:P}=e;return Object.assign(Object.assign({},he),{fontSize:P,lineHeight:o,titleFontWeight:s,borderRadius:r,border:`1px solid ${a}`,color:p,titleTextColor:c,iconColor:n,contentTextColor:n,closeBorderRadius:r,closeColorHover:d,closeColorPressed:h,closeIconColor:m,closeIconColorHover:C,closeIconColorPressed:u,borderInfo:`1px solid ${x(l,y(t,{alpha:.25}))}`,colorInfo:x(l,y(t,{alpha:.08})),titleTextColorInfo:c,iconColorInfo:t,contentTextColorInfo:n,closeColorHoverInfo:d,closeColorPressedInfo:h,closeIconColorInfo:m,closeIconColorHoverInfo:C,closeIconColorPressedInfo:u,borderSuccess:`1px solid ${x(l,y(z,{alpha:.25}))}`,colorSuccess:x(l,y(z,{alpha:.08})),titleTextColorSuccess:c,iconColorSuccess:z,contentTextColorSuccess:n,closeColorHoverSuccess:d,closeColorPressedSuccess:h,closeIconColorSuccess:m,closeIconColorHoverSuccess:C,closeIconColorPressedSuccess:u,borderWarning:`1px solid ${x(l,y(I,{alpha:.33}))}`,colorWarning:x(l,y(I,{alpha:.08})),titleTextColorWarning:c,iconColorWarning:I,contentTextColorWarning:n,closeColorHoverWarning:d,closeColorPressedWarning:h,closeIconColorWarning:m,closeIconColorHoverWarning:C,closeIconColorPressedWarning:u,borderError:`1px solid ${x(l,y(S,{alpha:.25}))}`,colorError:x(l,y(S,{alpha:.08})),titleTextColorError:c,iconColorError:S,contentTextColorError:n,closeColorHoverError:d,closeColorPressedError:h,closeIconColorError:m,closeIconColorHoverError:C,closeIconColorPressedError:u})}const pe={common:H,self:ge},fe=g("alert",`
 line-height: var(--n-line-height);
 border-radius: var(--n-border-radius);
 position: relative;
 transition: background-color .3s var(--n-bezier);
 background-color: var(--n-color);
 text-align: start;
 word-break: break-word;
`,[f("border",`
 border-radius: inherit;
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 transition: border-color .3s var(--n-bezier);
 border: var(--n-border);
 pointer-events: none;
 `),T("closable",[g("alert-body",[f("title",`
 padding-right: 24px;
 `)])]),f("icon",{color:"var(--n-icon-color)"}),g("alert-body",{padding:"var(--n-padding)"},[f("title",{color:"var(--n-title-text-color)"}),f("content",{color:"var(--n-content-text-color)"})]),K({originalTransition:"transform .3s var(--n-bezier)",enterToProps:{transform:"scale(1)"},leaveToProps:{transform:"scale(0.9)"}}),f("icon",`
 position: absolute;
 left: 0;
 top: 0;
 align-items: center;
 justify-content: center;
 display: flex;
 width: var(--n-icon-size);
 height: var(--n-icon-size);
 font-size: var(--n-icon-size);
 margin: var(--n-icon-margin);
 `),f("close",`
 transition:
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 position: absolute;
 right: 0;
 top: 0;
 margin: var(--n-close-margin);
 `),T("show-icon",[g("alert-body",{paddingLeft:"calc(var(--n-icon-margin-left) + var(--n-icon-size) + var(--n-icon-margin-right))"})]),T("right-adjust",[g("alert-body",{paddingRight:"calc(var(--n-close-size) + var(--n-padding) + 2px)"})]),g("alert-body",`
 border-radius: var(--n-border-radius);
 transition: border-color .3s var(--n-bezier);
 `,[f("title",`
 transition: color .3s var(--n-bezier);
 font-size: 16px;
 line-height: 19px;
 font-weight: var(--n-title-font-weight);
 `,[w("& +",[f("content",{marginTop:"9px"})])]),f("content",{transition:"color .3s var(--n-bezier)",fontSize:"var(--n-font-size)"})]),f("icon",{transition:"color .3s var(--n-bezier)"})]),ve=Object.assign(Object.assign({},R.props),{title:String,showIcon:{type:Boolean,default:!0},type:{type:String,default:"default"},bordered:{type:Boolean,default:!0},closable:Boolean,onClose:Function,onAfterLeave:Function,onAfterHide:Function}),Se=k({name:"Alert",inheritAttrs:!1,props:ve,slots:Object,setup(e){const{mergedClsPrefixRef:o,mergedBorderedRef:r,inlineThemeDisabled:s,mergedRtlRef:l}=W(e),a=R("Alert","-alert",fe,pe,e,o),p=J("Alert",l,o),c=$(()=>{const{common:{cubicBezierEaseInOut:u},self:t}=a.value,{fontSize:z,borderRadius:I,titleFontWeight:S,lineHeight:P,iconSize:_,iconMargin:E,iconMarginRtl:A,closeIconSize:L,closeBorderRadius:O,closeSize:M,closeMargin:N,closeMarginRtl:V,padding:D}=t,{type:b}=e,{left:F,right:q}=te(E);return{"--n-bezier":u,"--n-color":t[v("color",b)],"--n-close-icon-size":L,"--n-close-border-radius":O,"--n-close-color-hover":t[v("closeColorHover",b)],"--n-close-color-pressed":t[v("closeColorPressed",b)],"--n-close-icon-color":t[v("closeIconColor",b)],"--n-close-icon-color-hover":t[v("closeIconColorHover",b)],"--n-close-icon-color-pressed":t[v("closeIconColorPressed",b)],"--n-icon-color":t[v("iconColor",b)],"--n-border":t[v("border",b)],"--n-title-text-color":t[v("titleTextColor",b)],"--n-content-text-color":t[v("contentTextColor",b)],"--n-line-height":P,"--n-border-radius":I,"--n-font-size":z,"--n-title-font-weight":S,"--n-icon-size":_,"--n-icon-margin":E,"--n-icon-margin-rtl":A,"--n-close-size":M,"--n-close-margin":N,"--n-close-margin-rtl":V,"--n-padding":D,"--n-icon-margin-left":F,"--n-icon-margin-right":q}}),n=s?B("alert",$(()=>e.type[0]),c,e):void 0,d=j(!0),h=()=>{const{onAfterLeave:u,onAfterHide:t}=e;u&&u(),t&&t()};return{rtlEnabled:p,mergedClsPrefix:o,mergedBordered:r,visible:d,handleCloseClick:()=>{var u;Promise.resolve((u=e.onClose)===null||u===void 0?void 0:u.call(e)).then(t=>{t!==!1&&(d.value=!1)})},handleAfterLeave:()=>{h()},mergedTheme:a,cssVars:s?void 0:c,themeClass:n?.themeClass,onRender:n?.onRender}},render(){var e;return(e=this.onRender)===null||e===void 0||e.call(this),i(U,{onAfterLeave:this.handleAfterLeave},{default:()=>{const{mergedClsPrefix:o,$slots:r}=this,s={class:[`${o}-alert`,this.themeClass,this.closable&&`${o}-alert--closable`,this.showIcon&&`${o}-alert--show-icon`,!this.title&&this.closable&&`${o}-alert--right-adjust`,this.rtlEnabled&&`${o}-alert--rtl`],style:this.cssVars,role:"alert"};return this.visible?i("div",Object.assign({},Q(this.$attrs,s)),this.closable&&i(X,{clsPrefix:o,class:`${o}-alert__close`,onClick:this.handleCloseClick}),this.bordered&&i("div",{class:`${o}-alert__border`}),this.showIcon&&i("div",{class:`${o}-alert__icon`,"aria-hidden":"true"},Y(r.icon,()=>[i(G,{clsPrefix:o},{default:()=>{switch(this.type){case"success":return i(ne,null);case"info":return i(oe,null);case"warning":return i(ee,null);case"error":return i(Z,null);default:return null}}})])),i("div",{class:[`${o}-alert-body`,this.mergedBordered&&`${o}-alert-body--bordered`]},re(r.header,l=>{const a=l||this.title;return a?i("div",{class:`${o}-alert-body__title`},a):null}),r.default&&i("div",{class:`${o}-alert-body__content`},r))):null}})}});function be(e){const{opacityDisabled:o,heightTiny:r,heightSmall:s,heightMedium:l,heightLarge:a,heightHuge:p,primaryColor:c,fontSize:n}=e;return{fontSize:n,textColor:c,sizeTiny:r,sizeSmall:s,sizeMedium:l,sizeLarge:a,sizeHuge:p,color:c,opacitySpinning:o}}const me={common:H,self:be},Ce=w([w("@keyframes spin-rotate",`
 from {
 transform: rotate(0);
 }
 to {
 transform: rotate(360deg);
 }
 `),g("spin-container",`
 position: relative;
 `,[g("spin-body",`
 position: absolute;
 top: 50%;
 left: 50%;
 transform: translateX(-50%) translateY(-50%);
 `,[se()])]),g("spin-body",`
 display: inline-flex;
 align-items: center;
 justify-content: center;
 flex-direction: column;
 `),g("spin",`
 display: inline-flex;
 height: var(--n-size);
 width: var(--n-size);
 font-size: var(--n-size);
 color: var(--n-color);
 `,[T("rotate",`
 animation: spin-rotate 2s linear infinite;
 `)]),g("spin-description",`
 display: inline-block;
 font-size: var(--n-font-size);
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 margin-top: 8px;
 `),g("spin-content",`
 opacity: 1;
 transition: opacity .3s var(--n-bezier);
 pointer-events: all;
 `,[T("spinning",`
 user-select: none;
 -webkit-user-select: none;
 pointer-events: none;
 opacity: var(--n-opacity-spinning);
 `)])]),xe={small:20,medium:18,large:16},ye=Object.assign(Object.assign(Object.assign({},R.props),{contentClass:String,contentStyle:[Object,String],description:String,size:{type:[String,Number],default:"medium"},show:{type:Boolean,default:!0},rotate:{type:Boolean,default:!0},spinning:{type:Boolean,validator:()=>!0,default:void 0},delay:Number}),ce),Te=k({name:"Spin",props:ye,slots:Object,setup(e){const{mergedClsPrefixRef:o,inlineThemeDisabled:r}=W(e),s=R("Spin","-spin",Ce,me,e,o),l=$(()=>{const{size:n}=e,{common:{cubicBezierEaseInOut:d},self:h}=s.value,{opacitySpinning:m,color:C,textColor:u}=h,t=typeof n=="number"?de(n):h[v("size",n)];return{"--n-bezier":d,"--n-opacity-spinning":m,"--n-size":t,"--n-color":C,"--n-text-color":u}}),a=r?B("spin",$(()=>{const{size:n}=e;return typeof n=="number"?String(n):n[0]}),l,e):void 0,p=ue(e,["spinning","show"]),c=j(!1);return ae(n=>{let d;if(p.value){const{delay:h}=e;if(h){d=window.setTimeout(()=>{c.value=!0},h),n(()=>{clearTimeout(d)});return}}c.value=p.value}),{mergedClsPrefix:o,active:c,mergedStrokeWidth:$(()=>{const{strokeWidth:n}=e;if(n!==void 0)return n;const{size:d}=e;return xe[typeof d=="number"?"medium":d]}),cssVars:r?void 0:l,themeClass:a?.themeClass,onRender:a?.onRender}},render(){var e,o;const{$slots:r,mergedClsPrefix:s,description:l}=this,a=r.icon&&this.rotate,p=(l||r.description)&&i("div",{class:`${s}-spin-description`},l||((e=r.description)===null||e===void 0?void 0:e.call(r))),c=r.icon?i("div",{class:[`${s}-spin-body`,this.themeClass]},i("div",{class:[`${s}-spin`,a&&`${s}-spin--rotate`],style:r.default?"":this.cssVars},r.icon()),p):i("div",{class:[`${s}-spin-body`,this.themeClass]},i(ie,{clsPrefix:s,style:r.default?"":this.cssVars,stroke:this.stroke,"stroke-width":this.mergedStrokeWidth,radius:this.radius,scale:this.scale,class:`${s}-spin`}),p);return(o=this.onRender)===null||o===void 0||o.call(this),r.default?i("div",{class:[`${s}-spin-container`,this.themeClass],style:this.cssVars},i("div",{class:[`${s}-spin-content`,this.active&&`${s}-spin-content--spinning`,this.contentClass],style:this.contentStyle},r),i(le,{name:"fade-in-transition"},{default:()=>this.active?c:null})):c}});export{Se as N,Te as a};
