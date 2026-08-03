import{d as B,h as s,i as we,j as We,k as Se,l as Ce,m as re,n as X,p as u,q as w,S as Ae,t as ue,v as D,x as He,y as ve,z as C,r as F,A as U,C as c,D as I,E as Re,F as j,G as E,H as oe,I as q,J as Xe,K as V,L as me,M as ae,O as Ze,P as ne,Q as Je,V as Qe,R as xe,T as eo,U as oo,W as to,w as Q,u as Y,o as ro,a as W,X as ye,B as no,g as io,Y as lo,Z as ao,b as co,_ as so}from"./index-B19EQUzC.js";import{d as uo,t as vo,C as mo,N as ho,a as fo}from"./Dropdown-BvGHwYCe.js";import{u as ce}from"./use-merged-state-CqFd-6Rj.js";import{V as go,c as ie}from"./Popover-D5BcUa1e.js";import{u as po}from"./use-compitable-BU_QcteX.js";import{f as le}from"./get-gooblRlH.js";import{_ as bo}from"./_plugin-vue_export-helper-DlAUqK2U.js";import"./next-frame-once-C5Ksf8W7.js";const Co=B({name:"ChevronDownFilled",render(){return s("svg",{viewBox:"0 0 16 16",fill:"none",xmlns:"http://www.w3.org/2000/svg"},s("path",{d:"M3.20041 5.73966C3.48226 5.43613 3.95681 5.41856 4.26034 5.70041L8 9.22652L11.7397 5.70041C12.0432 5.41856 12.5177 5.43613 12.7996 5.73966C13.0815 6.0432 13.0639 6.51775 12.7603 6.7996L8.51034 10.7996C8.22258 11.0668 7.77743 11.0668 7.48967 10.7996L3.23966 6.7996C2.93613 6.51775 2.91856 6.0432 3.20041 5.73966Z",fill:"currentColor"}))}});function xo(e){const{baseColor:r,textColor2:o,bodyColor:n,cardColor:a,dividerColor:l,actionColor:m,scrollbarColor:p,scrollbarColorHover:d,invertedColor:h}=e;return{textColor:o,textColorInverted:"#FFF",color:n,colorEmbedded:m,headerColor:a,headerColorInverted:h,footerColor:m,footerColorInverted:h,headerBorderColor:l,headerBorderColorInverted:h,footerBorderColor:l,footerBorderColorInverted:h,siderBorderColor:l,siderBorderColorInverted:h,siderColor:a,siderColorInverted:h,siderToggleButtonBorder:`1px solid ${l}`,siderToggleButtonColor:r,siderToggleButtonIconColor:o,siderToggleButtonIconColorInverted:o,siderToggleBarColor:Ce(n,p),siderToggleBarColorHover:Ce(n,d),__invertScrollbar:"true"}}const Pe=we({name:"Layout",common:Se,peers:{Scrollbar:We},self:xo});function yo(e,r,o,n){return{itemColorHoverInverted:"#0000",itemColorActiveInverted:r,itemColorActiveHoverInverted:r,itemColorActiveCollapsedInverted:r,itemTextColorInverted:e,itemTextColorHoverInverted:o,itemTextColorChildActiveInverted:o,itemTextColorChildActiveHoverInverted:o,itemTextColorActiveInverted:o,itemTextColorActiveHoverInverted:o,itemTextColorHorizontalInverted:e,itemTextColorHoverHorizontalInverted:o,itemTextColorChildActiveHorizontalInverted:o,itemTextColorChildActiveHoverHorizontalInverted:o,itemTextColorActiveHorizontalInverted:o,itemTextColorActiveHoverHorizontalInverted:o,itemIconColorInverted:e,itemIconColorHoverInverted:o,itemIconColorActiveInverted:o,itemIconColorActiveHoverInverted:o,itemIconColorChildActiveInverted:o,itemIconColorChildActiveHoverInverted:o,itemIconColorCollapsedInverted:e,itemIconColorHorizontalInverted:e,itemIconColorHoverHorizontalInverted:o,itemIconColorActiveHorizontalInverted:o,itemIconColorActiveHoverHorizontalInverted:o,itemIconColorChildActiveHorizontalInverted:o,itemIconColorChildActiveHoverHorizontalInverted:o,arrowColorInverted:e,arrowColorHoverInverted:o,arrowColorActiveInverted:o,arrowColorActiveHoverInverted:o,arrowColorChildActiveInverted:o,arrowColorChildActiveHoverInverted:o,groupTextColorInverted:n}}function Io(e){const{borderRadius:r,textColor3:o,primaryColor:n,textColor2:a,textColor1:l,fontSize:m,dividerColor:p,hoverColor:d,primaryColorHover:h}=e;return Object.assign({borderRadius:r,color:"#0000",groupTextColor:o,itemColorHover:d,itemColorActive:re(n,{alpha:.1}),itemColorActiveHover:re(n,{alpha:.1}),itemColorActiveCollapsed:re(n,{alpha:.1}),itemTextColor:a,itemTextColorHover:a,itemTextColorActive:n,itemTextColorActiveHover:n,itemTextColorChildActive:n,itemTextColorChildActiveHover:n,itemTextColorHorizontal:a,itemTextColorHoverHorizontal:h,itemTextColorActiveHorizontal:n,itemTextColorActiveHoverHorizontal:n,itemTextColorChildActiveHorizontal:n,itemTextColorChildActiveHoverHorizontal:n,itemIconColor:l,itemIconColorHover:l,itemIconColorActive:n,itemIconColorActiveHover:n,itemIconColorChildActive:n,itemIconColorChildActiveHover:n,itemIconColorCollapsed:l,itemIconColorHorizontal:l,itemIconColorHoverHorizontal:h,itemIconColorActiveHorizontal:n,itemIconColorActiveHoverHorizontal:n,itemIconColorChildActiveHorizontal:n,itemIconColorChildActiveHoverHorizontal:n,itemHeight:"42px",arrowColor:a,arrowColorHover:a,arrowColorActive:n,arrowColorActiveHover:n,arrowColorChildActive:n,arrowColorChildActiveHover:n,colorInverted:"#0000",borderColorHorizontal:"#0000",fontSize:m,dividerColor:p},yo("#BBB",n,"#FFF","#AAA"))}const zo=we({name:"Menu",common:Se,peers:{Tooltip:vo,Dropdown:uo},self:Io}),Te=X("n-layout-sider"),Ne={type:String,default:"static"},wo=u("layout",`
 color: var(--n-text-color);
 background-color: var(--n-color);
 box-sizing: border-box;
 position: relative;
 z-index: auto;
 flex: auto;
 overflow: hidden;
 transition:
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
`,[u("layout-scroll-container",`
 overflow-x: hidden;
 box-sizing: border-box;
 height: 100%;
 `),w("absolute-positioned",`
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `)]),So={embedded:Boolean,position:Ne,nativeScrollbar:{type:Boolean,default:!0},scrollbarProps:Object,onScroll:Function,contentClass:String,contentStyle:{type:[String,Object],default:""},hasSider:Boolean,siderPlacement:{type:String,default:"left"}},_e=X("n-layout");function ke(e){return B({name:e?"LayoutContent":"Layout",props:Object.assign(Object.assign({},D.props),So),setup(r){const o=F(null),n=F(null),{mergedClsPrefixRef:a,inlineThemeDisabled:l}=ue(r),m=D("Layout","-layout",wo,Pe,r,a);function p(z,S){if(r.nativeScrollbar){const{value:P}=o;P&&(S===void 0?P.scrollTo(z):P.scrollTo(z,S))}else{const{value:P}=n;P&&P.scrollTo(z,S)}}U(_e,r);let d=0,h=0;const _=z=>{var S;const P=z.target;d=P.scrollLeft,h=P.scrollTop,(S=r.onScroll)===null||S===void 0||S.call(r,z)};He(()=>{if(r.nativeScrollbar){const z=o.value;z&&(z.scrollTop=h,z.scrollLeft=d)}});const N={display:"flex",flexWrap:"nowrap",width:"100%",flexDirection:"row"},f={scrollTo:p},T=C(()=>{const{common:{cubicBezierEaseInOut:z},self:S}=m.value;return{"--n-bezier":z,"--n-color":r.embedded?S.colorEmbedded:S.color,"--n-text-color":S.textColor}}),R=l?ve("layout",C(()=>r.embedded?"e":""),T,r):void 0;return Object.assign({mergedClsPrefix:a,scrollableElRef:o,scrollbarInstRef:n,hasSiderStyle:N,mergedTheme:m,handleNativeElScroll:_,cssVars:l?void 0:T,themeClass:R?.themeClass,onRender:R?.onRender},f)},render(){var r;const{mergedClsPrefix:o,hasSider:n}=this;(r=this.onRender)===null||r===void 0||r.call(this);const a=n?this.hasSiderStyle:void 0,l=[this.themeClass,e&&`${o}-layout-content`,`${o}-layout`,`${o}-layout--${this.position}-positioned`];return s("div",{class:l,style:this.cssVars},this.nativeScrollbar?s("div",{ref:"scrollableElRef",class:[`${o}-layout-scroll-container`,this.contentClass],style:[this.contentStyle,a],onScroll:this.handleNativeElScroll},this.$slots):s(Ae,Object.assign({},this.scrollbarProps,{onScroll:this.onScroll,ref:"scrollbarInstRef",theme:this.mergedTheme.peers.Scrollbar,themeOverrides:this.mergedTheme.peerOverrides.Scrollbar,contentClass:this.contentClass,contentStyle:[this.contentStyle,a]}),this.$slots))}})}const Ao=ke(!1),Ho=ke(!0),Ro=u("layout-sider",`
 flex-shrink: 0;
 box-sizing: border-box;
 position: relative;
 z-index: 1;
 color: var(--n-text-color);
 transition:
 color .3s var(--n-bezier),
 border-color .3s var(--n-bezier),
 min-width .3s var(--n-bezier),
 max-width .3s var(--n-bezier),
 transform .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 background-color: var(--n-color);
 display: flex;
 justify-content: flex-end;
`,[w("bordered",[c("border",`
 content: "";
 position: absolute;
 top: 0;
 bottom: 0;
 width: 1px;
 background-color: var(--n-border-color);
 transition: background-color .3s var(--n-bezier);
 `)]),c("left-placement",[w("bordered",[c("border",`
 right: 0;
 `)])]),w("right-placement",`
 justify-content: flex-start;
 `,[w("bordered",[c("border",`
 left: 0;
 `)]),w("collapsed",[u("layout-toggle-button",[u("base-icon",`
 transform: rotate(180deg);
 `)]),u("layout-toggle-bar",[I("&:hover",[c("top",{transform:"rotate(-12deg) scale(1.15) translateY(-2px)"}),c("bottom",{transform:"rotate(12deg) scale(1.15) translateY(2px)"})])])]),u("layout-toggle-button",`
 left: 0;
 transform: translateX(-50%) translateY(-50%);
 `,[u("base-icon",`
 transform: rotate(0);
 `)]),u("layout-toggle-bar",`
 left: -28px;
 transform: rotate(180deg);
 `,[I("&:hover",[c("top",{transform:"rotate(12deg) scale(1.15) translateY(-2px)"}),c("bottom",{transform:"rotate(-12deg) scale(1.15) translateY(2px)"})])])]),w("collapsed",[u("layout-toggle-bar",[I("&:hover",[c("top",{transform:"rotate(-12deg) scale(1.15) translateY(-2px)"}),c("bottom",{transform:"rotate(12deg) scale(1.15) translateY(2px)"})])]),u("layout-toggle-button",[u("base-icon",`
 transform: rotate(0);
 `)])]),u("layout-toggle-button",`
 transition:
 color .3s var(--n-bezier),
 right .3s var(--n-bezier),
 left .3s var(--n-bezier),
 border-color .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 cursor: pointer;
 width: 24px;
 height: 24px;
 position: absolute;
 top: 50%;
 right: 0;
 border-radius: 50%;
 display: flex;
 align-items: center;
 justify-content: center;
 font-size: 18px;
 color: var(--n-toggle-button-icon-color);
 border: var(--n-toggle-button-border);
 background-color: var(--n-toggle-button-color);
 box-shadow: 0 2px 4px 0px rgba(0, 0, 0, .06);
 transform: translateX(50%) translateY(-50%);
 z-index: 1;
 `,[u("base-icon",`
 transition: transform .3s var(--n-bezier);
 transform: rotate(180deg);
 `)]),u("layout-toggle-bar",`
 cursor: pointer;
 height: 72px;
 width: 32px;
 position: absolute;
 top: calc(50% - 36px);
 right: -28px;
 `,[c("top, bottom",`
 position: absolute;
 width: 4px;
 border-radius: 2px;
 height: 38px;
 left: 14px;
 transition: 
 background-color .3s var(--n-bezier),
 transform .3s var(--n-bezier);
 `),c("bottom",`
 position: absolute;
 top: 34px;
 `),I("&:hover",[c("top",{transform:"rotate(12deg) scale(1.15) translateY(-2px)"}),c("bottom",{transform:"rotate(-12deg) scale(1.15) translateY(2px)"})]),c("top, bottom",{backgroundColor:"var(--n-toggle-bar-color)"}),I("&:hover",[c("top, bottom",{backgroundColor:"var(--n-toggle-bar-color-hover)"})])]),c("border",`
 position: absolute;
 top: 0;
 right: 0;
 bottom: 0;
 width: 1px;
 transition: background-color .3s var(--n-bezier);
 `),u("layout-sider-scroll-container",`
 flex-grow: 1;
 flex-shrink: 0;
 box-sizing: border-box;
 height: 100%;
 opacity: 0;
 transition: opacity .3s var(--n-bezier);
 max-width: 100%;
 `),w("show-content",[u("layout-sider-scroll-container",{opacity:1})]),w("absolute-positioned",`
 position: absolute;
 left: 0;
 top: 0;
 bottom: 0;
 `)]),Po=B({props:{clsPrefix:{type:String,required:!0},onClick:Function},render(){const{clsPrefix:e}=this;return s("div",{onClick:this.onClick,class:`${e}-layout-toggle-bar`},s("div",{class:`${e}-layout-toggle-bar__top`}),s("div",{class:`${e}-layout-toggle-bar__bottom`}))}}),To=B({name:"LayoutToggleButton",props:{clsPrefix:{type:String,required:!0},onClick:Function},render(){const{clsPrefix:e}=this;return s("div",{class:`${e}-layout-toggle-button`,onClick:this.onClick},s(Re,{clsPrefix:e},{default:()=>s(mo,null)}))}}),No={position:Ne,bordered:Boolean,collapsedWidth:{type:Number,default:48},width:{type:[Number,String],default:272},contentClass:String,contentStyle:{type:[String,Object],default:""},collapseMode:{type:String,default:"transform"},collapsed:{type:Boolean,default:void 0},defaultCollapsed:Boolean,showCollapsedContent:{type:Boolean,default:!0},showTrigger:{type:[Boolean,String],default:!1},nativeScrollbar:{type:Boolean,default:!0},inverted:Boolean,scrollbarProps:Object,triggerClass:String,triggerStyle:[String,Object],collapsedTriggerClass:String,collapsedTriggerStyle:[String,Object],"onUpdate:collapsed":[Function,Array],onUpdateCollapsed:[Function,Array],onAfterEnter:Function,onAfterLeave:Function,onExpand:[Function,Array],onCollapse:[Function,Array],onScroll:Function},_o=B({name:"LayoutSider",props:Object.assign(Object.assign({},D.props),No),setup(e){const r=j(_e),o=F(null),n=F(null),a=F(e.defaultCollapsed),l=ce(oe(e,"collapsed"),a),m=C(()=>le(l.value?e.collapsedWidth:e.width)),p=C(()=>e.collapseMode!=="transform"?{}:{minWidth:le(e.width)}),d=C(()=>r?r.siderPlacement:"left");function h(H,x){if(e.nativeScrollbar){const{value:y}=o;y&&(x===void 0?y.scrollTo(H):y.scrollTo(H,x))}else{const{value:y}=n;y&&y.scrollTo(H,x)}}function _(){const{"onUpdate:collapsed":H,onUpdateCollapsed:x,onExpand:y,onCollapse:$}=e,{value:L}=l;x&&E(x,!L),H&&E(H,!L),a.value=!L,L?y&&E(y):$&&E($)}let N=0,f=0;const T=H=>{var x;const y=H.target;N=y.scrollLeft,f=y.scrollTop,(x=e.onScroll)===null||x===void 0||x.call(e,H)};He(()=>{if(e.nativeScrollbar){const H=o.value;H&&(H.scrollTop=f,H.scrollLeft=N)}}),U(Te,{collapsedRef:l,collapseModeRef:oe(e,"collapseMode")});const{mergedClsPrefixRef:R,inlineThemeDisabled:z}=ue(e),S=D("Layout","-layout-sider",Ro,Pe,e,R);function P(H){var x,y;H.propertyName==="max-width"&&(l.value?(x=e.onAfterLeave)===null||x===void 0||x.call(e):(y=e.onAfterEnter)===null||y===void 0||y.call(e))}const G={scrollTo:h},M=C(()=>{const{common:{cubicBezierEaseInOut:H},self:x}=S.value,{siderToggleButtonColor:y,siderToggleButtonBorder:$,siderToggleBarColor:L,siderToggleBarColorHover:te}=x,k={"--n-bezier":H,"--n-toggle-button-color":y,"--n-toggle-button-border":$,"--n-toggle-bar-color":L,"--n-toggle-bar-color-hover":te};return e.inverted?(k["--n-color"]=x.siderColorInverted,k["--n-text-color"]=x.textColorInverted,k["--n-border-color"]=x.siderBorderColorInverted,k["--n-toggle-button-icon-color"]=x.siderToggleButtonIconColorInverted,k.__invertScrollbar=x.__invertScrollbar):(k["--n-color"]=x.siderColor,k["--n-text-color"]=x.textColor,k["--n-border-color"]=x.siderBorderColor,k["--n-toggle-button-icon-color"]=x.siderToggleButtonIconColor),k}),O=z?ve("layout-sider",C(()=>e.inverted?"a":"b"),M,e):void 0;return Object.assign({scrollableElRef:o,scrollbarInstRef:n,mergedClsPrefix:R,mergedTheme:S,styleMaxWidth:m,mergedCollapsed:l,scrollContainerStyle:p,siderPlacement:d,handleNativeElScroll:T,handleTransitionend:P,handleTriggerClick:_,inlineThemeDisabled:z,cssVars:M,themeClass:O?.themeClass,onRender:O?.onRender},G)},render(){var e;const{mergedClsPrefix:r,mergedCollapsed:o,showTrigger:n}=this;return(e=this.onRender)===null||e===void 0||e.call(this),s("aside",{class:[`${r}-layout-sider`,this.themeClass,`${r}-layout-sider--${this.position}-positioned`,`${r}-layout-sider--${this.siderPlacement}-placement`,this.bordered&&`${r}-layout-sider--bordered`,o&&`${r}-layout-sider--collapsed`,(!o||this.showCollapsedContent)&&`${r}-layout-sider--show-content`],onTransitionend:this.handleTransitionend,style:[this.inlineThemeDisabled?void 0:this.cssVars,{maxWidth:this.styleMaxWidth,width:le(this.width)}]},this.nativeScrollbar?s("div",{class:[`${r}-layout-sider-scroll-container`,this.contentClass],onScroll:this.handleNativeElScroll,style:[this.scrollContainerStyle,{overflow:"auto"},this.contentStyle],ref:"scrollableElRef"},this.$slots):s(Ae,Object.assign({},this.scrollbarProps,{onScroll:this.onScroll,ref:"scrollbarInstRef",style:this.scrollContainerStyle,contentStyle:this.contentStyle,contentClass:this.contentClass,theme:this.mergedTheme.peers.Scrollbar,themeOverrides:this.mergedTheme.peerOverrides.Scrollbar,builtinThemeOverrides:this.inverted&&this.cssVars.__invertScrollbar==="true"?{colorHover:"rgba(255, 255, 255, .4)",color:"rgba(255, 255, 255, .3)"}:void 0}),this.$slots),n?n==="bar"?s(Po,{clsPrefix:r,class:o?this.collapsedTriggerClass:this.triggerClass,style:o?this.collapsedTriggerStyle:this.triggerStyle,onClick:this.handleTriggerClick}):s(To,{clsPrefix:r,class:o?this.collapsedTriggerClass:this.triggerClass,style:o?this.collapsedTriggerStyle:this.triggerStyle,onClick:this.handleTriggerClick}):null,this.bordered?s("div",{class:`${r}-layout-sider__border`}):null)}}),Z=X("n-menu"),Be=X("n-submenu"),he=X("n-menu-item-group"),Ie=[I("&::before","background-color: var(--n-item-color-hover);"),c("arrow",`
 color: var(--n-arrow-color-hover);
 `),c("icon",`
 color: var(--n-item-icon-color-hover);
 `),u("menu-item-content-header",`
 color: var(--n-item-text-color-hover);
 `,[I("a",`
 color: var(--n-item-text-color-hover);
 `),c("extra",`
 color: var(--n-item-text-color-hover);
 `)])],ze=[c("icon",`
 color: var(--n-item-icon-color-hover-horizontal);
 `),u("menu-item-content-header",`
 color: var(--n-item-text-color-hover-horizontal);
 `,[I("a",`
 color: var(--n-item-text-color-hover-horizontal);
 `),c("extra",`
 color: var(--n-item-text-color-hover-horizontal);
 `)])],ko=I([u("menu",`
 background-color: var(--n-color);
 color: var(--n-item-text-color);
 overflow: hidden;
 transition: background-color .3s var(--n-bezier);
 box-sizing: border-box;
 font-size: var(--n-font-size);
 padding-bottom: 6px;
 `,[w("horizontal",`
 max-width: 100%;
 width: 100%;
 display: flex;
 overflow: hidden;
 padding-bottom: 0;
 `,[u("submenu","margin: 0;"),u("menu-item","margin: 0;"),u("menu-item-content",`
 padding: 0 20px;
 border-bottom: 2px solid #0000;
 `,[I("&::before","display: none;"),w("selected","border-bottom: 2px solid var(--n-border-color-horizontal)")]),u("menu-item-content",[w("selected",[c("icon","color: var(--n-item-icon-color-active-horizontal);"),u("menu-item-content-header",`
 color: var(--n-item-text-color-active-horizontal);
 `,[I("a","color: var(--n-item-text-color-active-horizontal);"),c("extra","color: var(--n-item-text-color-active-horizontal);")])]),w("child-active",`
 border-bottom: 2px solid var(--n-border-color-horizontal);
 `,[u("menu-item-content-header",`
 color: var(--n-item-text-color-child-active-horizontal);
 `,[I("a",`
 color: var(--n-item-text-color-child-active-horizontal);
 `),c("extra",`
 color: var(--n-item-text-color-child-active-horizontal);
 `)]),c("icon",`
 color: var(--n-item-icon-color-child-active-horizontal);
 `)]),q("disabled",[q("selected, child-active",[I("&:focus-within",ze)]),w("selected",[K(null,[c("icon","color: var(--n-item-icon-color-active-hover-horizontal);"),u("menu-item-content-header",`
 color: var(--n-item-text-color-active-hover-horizontal);
 `,[I("a","color: var(--n-item-text-color-active-hover-horizontal);"),c("extra","color: var(--n-item-text-color-active-hover-horizontal);")])])]),w("child-active",[K(null,[c("icon","color: var(--n-item-icon-color-child-active-hover-horizontal);"),u("menu-item-content-header",`
 color: var(--n-item-text-color-child-active-hover-horizontal);
 `,[I("a","color: var(--n-item-text-color-child-active-hover-horizontal);"),c("extra","color: var(--n-item-text-color-child-active-hover-horizontal);")])])]),K("border-bottom: 2px solid var(--n-border-color-horizontal);",ze)]),u("menu-item-content-header",[I("a","color: var(--n-item-text-color-horizontal);")])])]),q("responsive",[u("menu-item-content-header",`
 overflow: hidden;
 text-overflow: ellipsis;
 `)]),w("collapsed",[u("menu-item-content",[w("selected",[I("&::before",`
 background-color: var(--n-item-color-active-collapsed) !important;
 `)]),u("menu-item-content-header","opacity: 0;"),c("arrow","opacity: 0;"),c("icon","color: var(--n-item-icon-color-collapsed);")])]),u("menu-item",`
 height: var(--n-item-height);
 margin-top: 6px;
 position: relative;
 `),u("menu-item-content",`
 box-sizing: border-box;
 line-height: 1.75;
 height: 100%;
 display: grid;
 grid-template-areas: "icon content arrow";
 grid-template-columns: auto 1fr auto;
 align-items: center;
 cursor: pointer;
 position: relative;
 padding-right: 18px;
 transition:
 background-color .3s var(--n-bezier),
 padding-left .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 `,[I("> *","z-index: 1;"),I("&::before",`
 z-index: auto;
 content: "";
 background-color: #0000;
 position: absolute;
 left: 8px;
 right: 8px;
 top: 0;
 bottom: 0;
 pointer-events: none;
 border-radius: var(--n-border-radius);
 transition: background-color .3s var(--n-bezier);
 `),w("disabled",`
 opacity: .45;
 cursor: not-allowed;
 `),w("collapsed",[c("arrow","transform: rotate(0);")]),w("selected",[I("&::before","background-color: var(--n-item-color-active);"),c("arrow","color: var(--n-arrow-color-active);"),c("icon","color: var(--n-item-icon-color-active);"),u("menu-item-content-header",`
 color: var(--n-item-text-color-active);
 `,[I("a","color: var(--n-item-text-color-active);"),c("extra","color: var(--n-item-text-color-active);")])]),w("child-active",[u("menu-item-content-header",`
 color: var(--n-item-text-color-child-active);
 `,[I("a",`
 color: var(--n-item-text-color-child-active);
 `),c("extra",`
 color: var(--n-item-text-color-child-active);
 `)]),c("arrow",`
 color: var(--n-arrow-color-child-active);
 `),c("icon",`
 color: var(--n-item-icon-color-child-active);
 `)]),q("disabled",[q("selected, child-active",[I("&:focus-within",Ie)]),w("selected",[K(null,[c("arrow","color: var(--n-arrow-color-active-hover);"),c("icon","color: var(--n-item-icon-color-active-hover);"),u("menu-item-content-header",`
 color: var(--n-item-text-color-active-hover);
 `,[I("a","color: var(--n-item-text-color-active-hover);"),c("extra","color: var(--n-item-text-color-active-hover);")])])]),w("child-active",[K(null,[c("arrow","color: var(--n-arrow-color-child-active-hover);"),c("icon","color: var(--n-item-icon-color-child-active-hover);"),u("menu-item-content-header",`
 color: var(--n-item-text-color-child-active-hover);
 `,[I("a","color: var(--n-item-text-color-child-active-hover);"),c("extra","color: var(--n-item-text-color-child-active-hover);")])])]),w("selected",[K(null,[I("&::before","background-color: var(--n-item-color-active-hover);")])]),K(null,Ie)]),c("icon",`
 grid-area: icon;
 color: var(--n-item-icon-color);
 transition:
 color .3s var(--n-bezier),
 font-size .3s var(--n-bezier),
 margin-right .3s var(--n-bezier);
 box-sizing: content-box;
 display: inline-flex;
 align-items: center;
 justify-content: center;
 `),c("arrow",`
 grid-area: arrow;
 font-size: 16px;
 color: var(--n-arrow-color);
 transform: rotate(180deg);
 opacity: 1;
 transition:
 color .3s var(--n-bezier),
 transform 0.2s var(--n-bezier),
 opacity 0.2s var(--n-bezier);
 `),u("menu-item-content-header",`
 grid-area: content;
 transition:
 color .3s var(--n-bezier),
 opacity .3s var(--n-bezier);
 opacity: 1;
 white-space: nowrap;
 color: var(--n-item-text-color);
 `,[I("a",`
 outline: none;
 text-decoration: none;
 transition: color .3s var(--n-bezier);
 color: var(--n-item-text-color);
 `,[I("&::before",`
 content: "";
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `)]),c("extra",`
 font-size: .93em;
 color: var(--n-group-text-color);
 transition: color .3s var(--n-bezier);
 `)])]),u("submenu",`
 cursor: pointer;
 position: relative;
 margin-top: 6px;
 `,[u("menu-item-content",`
 height: var(--n-item-height);
 `),u("submenu-children",`
 overflow: hidden;
 padding: 0;
 `,[Xe({duration:".2s"})])]),u("menu-item-group",[u("menu-item-group-title",`
 margin-top: 6px;
 color: var(--n-group-text-color);
 cursor: default;
 font-size: .93em;
 height: 36px;
 display: flex;
 align-items: center;
 transition:
 padding-left .3s var(--n-bezier),
 color .3s var(--n-bezier);
 `)])]),u("menu-tooltip",[I("a",`
 color: inherit;
 text-decoration: none;
 `)]),u("menu-divider",`
 transition: background-color .3s var(--n-bezier);
 background-color: var(--n-divider-color);
 height: 1px;
 margin: 6px 18px;
 `)]);function K(e,r){return[w("hover",e,r),I("&:hover",e,r)]}const Oe=B({name:"MenuOptionContent",props:{collapsed:Boolean,disabled:Boolean,title:[String,Function],icon:Function,extra:[String,Function],showArrow:Boolean,childActive:Boolean,hover:Boolean,paddingLeft:Number,selected:Boolean,maxIconSize:{type:Number,required:!0},activeIconSize:{type:Number,required:!0},iconMarginRight:{type:Number,required:!0},clsPrefix:{type:String,required:!0},onClick:Function,tmNode:{type:Object,required:!0},isEllipsisPlaceholder:Boolean},setup(e){const{props:r}=j(Z);return{menuProps:r,style:C(()=>{const{paddingLeft:o}=e;return{paddingLeft:o&&`${o}px`}}),iconStyle:C(()=>{const{maxIconSize:o,activeIconSize:n,iconMarginRight:a}=e;return{width:`${o}px`,height:`${o}px`,fontSize:`${n}px`,marginRight:`${a}px`}})}},render(){const{clsPrefix:e,tmNode:r,menuProps:{renderIcon:o,renderLabel:n,renderExtra:a,expandIcon:l}}=this,m=o?o(r.rawNode):V(this.icon);return s("div",{onClick:p=>{var d;(d=this.onClick)===null||d===void 0||d.call(this,p)},role:"none",class:[`${e}-menu-item-content`,{[`${e}-menu-item-content--selected`]:this.selected,[`${e}-menu-item-content--collapsed`]:this.collapsed,[`${e}-menu-item-content--child-active`]:this.childActive,[`${e}-menu-item-content--disabled`]:this.disabled,[`${e}-menu-item-content--hover`]:this.hover}],style:this.style},m&&s("div",{class:`${e}-menu-item-content__icon`,style:this.iconStyle,role:"none"},[m]),s("div",{class:`${e}-menu-item-content-header`,role:"none"},this.isEllipsisPlaceholder?this.title:n?n(r.rawNode):V(this.title),this.extra||a?s("span",{class:`${e}-menu-item-content-header__extra`}," ",a?a(r.rawNode):V(this.extra)):null),this.showArrow?s(Re,{ariaHidden:!0,class:`${e}-menu-item-content__arrow`,clsPrefix:e},{default:()=>l?l(r.rawNode):s(Co,null)}):null)}}),ee=8;function fe(e){const r=j(Z),{props:o,mergedCollapsedRef:n}=r,a=j(Be,null),l=j(he,null),m=C(()=>o.mode==="horizontal"),p=C(()=>m.value?o.dropdownPlacement:"tmNodes"in e?"right-start":"right"),d=C(()=>{var f;return Math.max((f=o.collapsedIconSize)!==null&&f!==void 0?f:o.iconSize,o.iconSize)}),h=C(()=>{var f;return!m.value&&e.root&&n.value&&(f=o.collapsedIconSize)!==null&&f!==void 0?f:o.iconSize}),_=C(()=>{if(m.value)return;const{collapsedWidth:f,indent:T,rootIndent:R}=o,{root:z,isGroup:S}=e,P=R===void 0?T:R;return z?n.value?f/2-d.value/2:P:l&&typeof l.paddingLeftRef.value=="number"?T/2+l.paddingLeftRef.value:a&&typeof a.paddingLeftRef.value=="number"?(S?T/2:T)+a.paddingLeftRef.value:0}),N=C(()=>{const{collapsedWidth:f,indent:T,rootIndent:R}=o,{value:z}=d,{root:S}=e;return m.value||!S||!n.value?ee:(R===void 0?T:R)+z+ee-(f+z)/2});return{dropdownPlacement:p,activeIconSize:h,maxIconSize:d,paddingLeft:_,iconMarginRight:N,NMenu:r,NSubmenu:a,NMenuOptionGroup:l}}const ge={internalKey:{type:[String,Number],required:!0},root:Boolean,isGroup:Boolean,level:{type:Number,required:!0},title:[String,Function],extra:[String,Function]},Bo=B({name:"MenuDivider",setup(){const e=j(Z),{mergedClsPrefixRef:r,isHorizontalRef:o}=e;return()=>o.value?null:s("div",{class:`${r.value}-menu-divider`})}}),Ee=Object.assign(Object.assign({},ge),{tmNode:{type:Object,required:!0},disabled:Boolean,icon:Function,onClick:Function}),Oo=me(Ee),Eo=B({name:"MenuOption",props:Ee,setup(e){const r=fe(e),{NSubmenu:o,NMenu:n,NMenuOptionGroup:a}=r,{props:l,mergedClsPrefixRef:m,mergedCollapsedRef:p}=n,d=o?o.mergedDisabledRef:a?a.mergedDisabledRef:{value:!1},h=C(()=>d.value||e.disabled);function _(f){const{onClick:T}=e;T&&T(f)}function N(f){h.value||(n.doSelect(e.internalKey,e.tmNode.rawNode),_(f))}return{mergedClsPrefix:m,dropdownPlacement:r.dropdownPlacement,paddingLeft:r.paddingLeft,iconMarginRight:r.iconMarginRight,maxIconSize:r.maxIconSize,activeIconSize:r.activeIconSize,mergedTheme:n.mergedThemeRef,menuProps:l,dropdownEnabled:ae(()=>e.root&&p.value&&l.mode!=="horizontal"&&!h.value),selected:ae(()=>n.mergedValueRef.value===e.internalKey),mergedDisabled:h,handleClick:N}},render(){const{mergedClsPrefix:e,mergedTheme:r,tmNode:o,menuProps:{renderLabel:n,nodeProps:a}}=this,l=a?.(o.rawNode);return s("div",Object.assign({},l,{role:"menuitem",class:[`${e}-menu-item`,l?.class]}),s(ho,{theme:r.peers.Tooltip,themeOverrides:r.peerOverrides.Tooltip,trigger:"hover",placement:this.dropdownPlacement,disabled:!this.dropdownEnabled||this.title===void 0,internalExtraClass:["menu-tooltip"]},{default:()=>n?n(o.rawNode):V(this.title),trigger:()=>s(Oe,{tmNode:o,clsPrefix:e,paddingLeft:this.paddingLeft,iconMarginRight:this.iconMarginRight,maxIconSize:this.maxIconSize,activeIconSize:this.activeIconSize,selected:this.selected,title:this.title,extra:this.extra,disabled:this.mergedDisabled,icon:this.icon,onClick:this.handleClick})}))}}),Fe=Object.assign(Object.assign({},ge),{tmNode:{type:Object,required:!0},tmNodes:{type:Array,required:!0}}),Fo=me(Fe),Lo=B({name:"MenuOptionGroup",props:Fe,setup(e){const r=fe(e),{NSubmenu:o}=r,n=C(()=>o?.mergedDisabledRef.value?!0:e.tmNode.disabled);U(he,{paddingLeftRef:r.paddingLeft,mergedDisabledRef:n});const{mergedClsPrefixRef:a,props:l}=j(Z);return function(){const{value:m}=a,p=r.paddingLeft.value,{nodeProps:d}=l,h=d?.(e.tmNode.rawNode);return s("div",{class:`${m}-menu-item-group`,role:"group"},s("div",Object.assign({},h,{class:[`${m}-menu-item-group-title`,h?.class],style:[h?.style||"",p!==void 0?`padding-left: ${p}px;`:""]}),V(e.title),e.extra?s(Ze,null," ",V(e.extra)):null),s("div",null,e.tmNodes.map(_=>pe(_,l))))}}});function de(e){return e.type==="divider"||e.type==="render"}function Mo(e){return e.type==="divider"}function pe(e,r){const{rawNode:o}=e,{show:n}=o;if(n===!1)return null;if(de(o))return Mo(o)?s(Bo,Object.assign({key:e.key},o.props)):null;const{labelField:a}=r,{key:l,level:m,isGroup:p}=e,d=Object.assign(Object.assign({},o),{title:o.title||o[a],extra:o.titleExtra||o.extra,key:l,internalKey:l,level:m,root:m===0,isGroup:p});return e.children?e.isGroup?s(Lo,ne(d,Fo,{tmNode:e,tmNodes:e.children,key:l})):s(se,ne(d,$o,{key:l,rawNodes:o[r.childrenField],tmNodes:e.children,tmNode:e})):s(Eo,ne(d,Oo,{key:l,tmNode:e}))}const Le=Object.assign(Object.assign({},ge),{rawNodes:{type:Array,default:()=>[]},tmNodes:{type:Array,default:()=>[]},tmNode:{type:Object,required:!0},disabled:Boolean,icon:Function,onClick:Function,domId:String,virtualChildActive:{type:Boolean,default:void 0},isEllipsisPlaceholder:Boolean}),$o=me(Le),se=B({name:"Submenu",props:Le,setup(e){const r=fe(e),{NMenu:o,NSubmenu:n}=r,{props:a,mergedCollapsedRef:l,mergedThemeRef:m}=o,p=C(()=>{const{disabled:f}=e;return n?.mergedDisabledRef.value||a.disabled?!0:f}),d=F(!1);U(Be,{paddingLeftRef:r.paddingLeft,mergedDisabledRef:p}),U(he,null);function h(){const{onClick:f}=e;f&&f()}function _(){p.value||(l.value||o.toggleExpand(e.internalKey),h())}function N(f){d.value=f}return{menuProps:a,mergedTheme:m,doSelect:o.doSelect,inverted:o.invertedRef,isHorizontal:o.isHorizontalRef,mergedClsPrefix:o.mergedClsPrefixRef,maxIconSize:r.maxIconSize,activeIconSize:r.activeIconSize,iconMarginRight:r.iconMarginRight,dropdownPlacement:r.dropdownPlacement,dropdownShow:d,paddingLeft:r.paddingLeft,mergedDisabled:p,mergedValue:o.mergedValueRef,childActive:ae(()=>{var f;return(f=e.virtualChildActive)!==null&&f!==void 0?f:o.activePathRef.value.includes(e.internalKey)}),collapsed:C(()=>a.mode==="horizontal"?!1:l.value?!0:!o.mergedExpandedKeysRef.value.includes(e.internalKey)),dropdownEnabled:C(()=>!p.value&&(a.mode==="horizontal"||l.value)),handlePopoverShowChange:N,handleClick:_}},render(){var e;const{mergedClsPrefix:r,menuProps:{renderIcon:o,renderLabel:n}}=this,a=()=>{const{isHorizontal:m,paddingLeft:p,collapsed:d,mergedDisabled:h,maxIconSize:_,activeIconSize:N,title:f,childActive:T,icon:R,handleClick:z,menuProps:{nodeProps:S},dropdownShow:P,iconMarginRight:G,tmNode:M,mergedClsPrefix:O,isEllipsisPlaceholder:H,extra:x}=this,y=S?.(M.rawNode);return s("div",Object.assign({},y,{class:[`${O}-menu-item`,y?.class],role:"menuitem"}),s(Oe,{tmNode:M,paddingLeft:p,collapsed:d,disabled:h,iconMarginRight:G,maxIconSize:_,activeIconSize:N,title:f,extra:x,showArrow:!m,childActive:T,clsPrefix:O,icon:R,hover:P,onClick:z,isEllipsisPlaceholder:H}))},l=()=>s(Je,null,{default:()=>{const{tmNodes:m,collapsed:p}=this;return p?null:s("div",{class:`${r}-submenu-children`,role:"menu"},m.map(d=>pe(d,this.menuProps)))}});return this.root?s(fo,Object.assign({size:"large",trigger:"hover"},(e=this.menuProps)===null||e===void 0?void 0:e.dropdownProps,{themeOverrides:this.mergedTheme.peerOverrides.Dropdown,theme:this.mergedTheme.peers.Dropdown,builtinThemeOverrides:{fontSizeLarge:"14px",optionIconSizeLarge:"18px"},value:this.mergedValue,disabled:!this.dropdownEnabled,placement:this.dropdownPlacement,keyField:this.menuProps.keyField,labelField:this.menuProps.labelField,childrenField:this.menuProps.childrenField,onUpdateShow:this.handlePopoverShowChange,options:this.rawNodes,onSelect:this.doSelect,inverted:this.inverted,renderIcon:o,renderLabel:n}),{default:()=>s("div",{class:`${r}-submenu`,role:"menu","aria-expanded":!this.collapsed,id:this.domId},a(),this.isHorizontal?null:l())}):s("div",{class:`${r}-submenu`,role:"menu","aria-expanded":!this.collapsed,id:this.domId},a(),l())}}),jo=Object.assign(Object.assign({},D.props),{options:{type:Array,default:()=>[]},collapsed:{type:Boolean,default:void 0},collapsedWidth:{type:Number,default:48},iconSize:{type:Number,default:20},collapsedIconSize:{type:Number,default:24},rootIndent:Number,indent:{type:Number,default:32},labelField:{type:String,default:"label"},keyField:{type:String,default:"key"},childrenField:{type:String,default:"children"},disabledField:{type:String,default:"disabled"},defaultExpandAll:Boolean,defaultExpandedKeys:Array,expandedKeys:Array,value:[String,Number],defaultValue:{type:[String,Number],default:null},mode:{type:String,default:"vertical"},watchProps:{type:Array,default:void 0},disabled:Boolean,show:{type:Boolean,default:!0},inverted:Boolean,"onUpdate:expandedKeys":[Function,Array],onUpdateExpandedKeys:[Function,Array],onUpdateValue:[Function,Array],"onUpdate:value":[Function,Array],expandIcon:Function,renderIcon:Function,renderLabel:Function,renderExtra:Function,dropdownProps:Object,accordion:Boolean,nodeProps:Function,dropdownPlacement:{type:String,default:"bottom"},responsive:Boolean,items:Array,onOpenNamesChange:[Function,Array],onSelect:[Function,Array],onExpandedNamesChange:[Function,Array],expandedNames:Array,defaultExpandedNames:Array}),Ko=B({name:"Menu",inheritAttrs:!1,props:jo,setup(e){const{mergedClsPrefixRef:r,inlineThemeDisabled:o}=ue(e),n=D("Menu","-menu",ko,zo,e,r),a=j(Te,null),l=C(()=>{var v;const{collapsed:b}=e;if(b!==void 0)return b;if(a){const{collapseModeRef:t,collapsedRef:g}=a;if(t.value==="width")return(v=g.value)!==null&&v!==void 0?v:!1}return!1}),m=C(()=>{const{keyField:v,childrenField:b,disabledField:t}=e;return ie(e.items||e.options,{getIgnored(g){return de(g)},getChildren(g){return g[b]},getDisabled(g){return g[t]},getKey(g){var A;return(A=g[v])!==null&&A!==void 0?A:g.name}})}),p=C(()=>new Set(m.value.treeNodes.map(v=>v.key))),{watchProps:d}=e,h=F(null);d?.includes("defaultValue")?xe(()=>{h.value=e.defaultValue}):h.value=e.defaultValue;const _=oe(e,"value"),N=ce(_,h),f=F([]),T=()=>{f.value=e.defaultExpandAll?m.value.getNonLeafKeys():e.defaultExpandedNames||e.defaultExpandedKeys||m.value.getPath(N.value,{includeSelf:!1}).keyPath};d?.includes("defaultExpandedKeys")?xe(T):T();const R=po(e,["expandedNames","expandedKeys"]),z=ce(R,f),S=C(()=>m.value.treeNodes),P=C(()=>m.value.getPath(N.value).keyPath);U(Z,{props:e,mergedCollapsedRef:l,mergedThemeRef:n,mergedValueRef:N,mergedExpandedKeysRef:z,activePathRef:P,mergedClsPrefixRef:r,isHorizontalRef:C(()=>e.mode==="horizontal"),invertedRef:oe(e,"inverted"),doSelect:G,toggleExpand:O});function G(v,b){const{"onUpdate:value":t,onUpdateValue:g,onSelect:A}=e;g&&E(g,v,b),t&&E(t,v,b),A&&E(A,v,b),h.value=v}function M(v){const{"onUpdate:expandedKeys":b,onUpdateExpandedKeys:t,onExpandedNamesChange:g,onOpenNamesChange:A}=e;b&&E(b,v),t&&E(t,v),g&&E(g,v),A&&E(A,v),f.value=v}function O(v){const b=Array.from(z.value),t=b.findIndex(g=>g===v);if(~t)b.splice(t,1);else{if(e.accordion&&p.value.has(v)){const g=b.findIndex(A=>p.value.has(A));g>-1&&b.splice(g,1)}b.push(v)}M(b)}const H=v=>{const b=m.value.getPath(v??N.value,{includeSelf:!1}).keyPath;if(!b.length)return;const t=Array.from(z.value),g=new Set([...t,...b]);e.accordion&&p.value.forEach(A=>{g.has(A)&&!b.includes(A)&&g.delete(A)}),M(Array.from(g))},x=C(()=>{const{inverted:v}=e,{common:{cubicBezierEaseInOut:b},self:t}=n.value,{borderRadius:g,borderColorHorizontal:A,fontSize:Ge,itemHeight:qe,dividerColor:Ye}=t,i={"--n-divider-color":Ye,"--n-bezier":b,"--n-font-size":Ge,"--n-border-color-horizontal":A,"--n-border-radius":g,"--n-item-height":qe};return v?(i["--n-group-text-color"]=t.groupTextColorInverted,i["--n-color"]=t.colorInverted,i["--n-item-text-color"]=t.itemTextColorInverted,i["--n-item-text-color-hover"]=t.itemTextColorHoverInverted,i["--n-item-text-color-active"]=t.itemTextColorActiveInverted,i["--n-item-text-color-child-active"]=t.itemTextColorChildActiveInverted,i["--n-item-text-color-child-active-hover"]=t.itemTextColorChildActiveInverted,i["--n-item-text-color-active-hover"]=t.itemTextColorActiveHoverInverted,i["--n-item-icon-color"]=t.itemIconColorInverted,i["--n-item-icon-color-hover"]=t.itemIconColorHoverInverted,i["--n-item-icon-color-active"]=t.itemIconColorActiveInverted,i["--n-item-icon-color-active-hover"]=t.itemIconColorActiveHoverInverted,i["--n-item-icon-color-child-active"]=t.itemIconColorChildActiveInverted,i["--n-item-icon-color-child-active-hover"]=t.itemIconColorChildActiveHoverInverted,i["--n-item-icon-color-collapsed"]=t.itemIconColorCollapsedInverted,i["--n-item-text-color-horizontal"]=t.itemTextColorHorizontalInverted,i["--n-item-text-color-hover-horizontal"]=t.itemTextColorHoverHorizontalInverted,i["--n-item-text-color-active-horizontal"]=t.itemTextColorActiveHorizontalInverted,i["--n-item-text-color-child-active-horizontal"]=t.itemTextColorChildActiveHorizontalInverted,i["--n-item-text-color-child-active-hover-horizontal"]=t.itemTextColorChildActiveHoverHorizontalInverted,i["--n-item-text-color-active-hover-horizontal"]=t.itemTextColorActiveHoverHorizontalInverted,i["--n-item-icon-color-horizontal"]=t.itemIconColorHorizontalInverted,i["--n-item-icon-color-hover-horizontal"]=t.itemIconColorHoverHorizontalInverted,i["--n-item-icon-color-active-horizontal"]=t.itemIconColorActiveHorizontalInverted,i["--n-item-icon-color-active-hover-horizontal"]=t.itemIconColorActiveHoverHorizontalInverted,i["--n-item-icon-color-child-active-horizontal"]=t.itemIconColorChildActiveHorizontalInverted,i["--n-item-icon-color-child-active-hover-horizontal"]=t.itemIconColorChildActiveHoverHorizontalInverted,i["--n-arrow-color"]=t.arrowColorInverted,i["--n-arrow-color-hover"]=t.arrowColorHoverInverted,i["--n-arrow-color-active"]=t.arrowColorActiveInverted,i["--n-arrow-color-active-hover"]=t.arrowColorActiveHoverInverted,i["--n-arrow-color-child-active"]=t.arrowColorChildActiveInverted,i["--n-arrow-color-child-active-hover"]=t.arrowColorChildActiveHoverInverted,i["--n-item-color-hover"]=t.itemColorHoverInverted,i["--n-item-color-active"]=t.itemColorActiveInverted,i["--n-item-color-active-hover"]=t.itemColorActiveHoverInverted,i["--n-item-color-active-collapsed"]=t.itemColorActiveCollapsedInverted):(i["--n-group-text-color"]=t.groupTextColor,i["--n-color"]=t.color,i["--n-item-text-color"]=t.itemTextColor,i["--n-item-text-color-hover"]=t.itemTextColorHover,i["--n-item-text-color-active"]=t.itemTextColorActive,i["--n-item-text-color-child-active"]=t.itemTextColorChildActive,i["--n-item-text-color-child-active-hover"]=t.itemTextColorChildActiveHover,i["--n-item-text-color-active-hover"]=t.itemTextColorActiveHover,i["--n-item-icon-color"]=t.itemIconColor,i["--n-item-icon-color-hover"]=t.itemIconColorHover,i["--n-item-icon-color-active"]=t.itemIconColorActive,i["--n-item-icon-color-active-hover"]=t.itemIconColorActiveHover,i["--n-item-icon-color-child-active"]=t.itemIconColorChildActive,i["--n-item-icon-color-child-active-hover"]=t.itemIconColorChildActiveHover,i["--n-item-icon-color-collapsed"]=t.itemIconColorCollapsed,i["--n-item-text-color-horizontal"]=t.itemTextColorHorizontal,i["--n-item-text-color-hover-horizontal"]=t.itemTextColorHoverHorizontal,i["--n-item-text-color-active-horizontal"]=t.itemTextColorActiveHorizontal,i["--n-item-text-color-child-active-horizontal"]=t.itemTextColorChildActiveHorizontal,i["--n-item-text-color-child-active-hover-horizontal"]=t.itemTextColorChildActiveHoverHorizontal,i["--n-item-text-color-active-hover-horizontal"]=t.itemTextColorActiveHoverHorizontal,i["--n-item-icon-color-horizontal"]=t.itemIconColorHorizontal,i["--n-item-icon-color-hover-horizontal"]=t.itemIconColorHoverHorizontal,i["--n-item-icon-color-active-horizontal"]=t.itemIconColorActiveHorizontal,i["--n-item-icon-color-active-hover-horizontal"]=t.itemIconColorActiveHoverHorizontal,i["--n-item-icon-color-child-active-horizontal"]=t.itemIconColorChildActiveHorizontal,i["--n-item-icon-color-child-active-hover-horizontal"]=t.itemIconColorChildActiveHoverHorizontal,i["--n-arrow-color"]=t.arrowColor,i["--n-arrow-color-hover"]=t.arrowColorHover,i["--n-arrow-color-active"]=t.arrowColorActive,i["--n-arrow-color-active-hover"]=t.arrowColorActiveHover,i["--n-arrow-color-child-active"]=t.arrowColorChildActive,i["--n-arrow-color-child-active-hover"]=t.arrowColorChildActiveHover,i["--n-item-color-hover"]=t.itemColorHover,i["--n-item-color-active"]=t.itemColorActive,i["--n-item-color-active-hover"]=t.itemColorActiveHover,i["--n-item-color-active-collapsed"]=t.itemColorActiveCollapsed),i}),y=o?ve("menu",C(()=>e.inverted?"a":"b"),x,e):void 0,$=eo(),L=F(null),te=F(null);let k=!0;const be=()=>{var v;k?k=!1:(v=L.value)===null||v===void 0||v.sync({showAllItemsBeforeCalculate:!0})};function Me(){return document.getElementById($)}const J=F(-1);function $e(v){J.value=e.options.length-v}function je(v){v||(J.value=-1)}const Ke=C(()=>{const v=J.value;return{children:v===-1?[]:e.options.slice(v)}}),Ve=C(()=>{const{childrenField:v,disabledField:b,keyField:t}=e;return ie([Ke.value],{getIgnored(g){return de(g)},getChildren(g){return g[v]},getDisabled(g){return g[b]},getKey(g){var A;return(A=g[t])!==null&&A!==void 0?A:g.name}})}),De=C(()=>ie([{}]).treeNodes[0]);function Ue(){var v;if(J.value===-1)return s(se,{root:!0,level:0,key:"__ellpisisGroupPlaceholder__",internalKey:"__ellpisisGroupPlaceholder__",title:"···",tmNode:De.value,domId:$,isEllipsisPlaceholder:!0});const b=Ve.value.treeNodes[0],t=P.value,g=!!(!((v=b.children)===null||v===void 0)&&v.some(A=>t.includes(A.key)));return s(se,{level:0,root:!0,key:"__ellpisisGroup__",internalKey:"__ellpisisGroup__",title:"···",virtualChildActive:g,tmNode:b,domId:$,rawNodes:b.rawNode.children||[],tmNodes:b.children||[],isEllipsisPlaceholder:!0})}return{mergedClsPrefix:r,controlledExpandedKeys:R,uncontrolledExpanededKeys:f,mergedExpandedKeys:z,uncontrolledValue:h,mergedValue:N,activePath:P,tmNodes:S,mergedTheme:n,mergedCollapsed:l,cssVars:o?void 0:x,themeClass:y?.themeClass,overflowRef:L,counterRef:te,updateCounter:()=>{},onResize:be,onUpdateOverflow:je,onUpdateCount:$e,renderCounter:Ue,getCounter:Me,onRender:y?.onRender,showOption:H,deriveResponsiveState:be}},render(){const{mergedClsPrefix:e,mode:r,themeClass:o,onRender:n}=this;n?.();const a=()=>this.tmNodes.map(d=>pe(d,this.$props)),m=r==="horizontal"&&this.responsive,p=()=>s("div",oo(this.$attrs,{role:r==="horizontal"?"menubar":"menu",class:[`${e}-menu`,o,`${e}-menu--${r}`,m&&`${e}-menu--responsive`,this.mergedCollapsed&&`${e}-menu--collapsed`],style:this.cssVars}),m?s(go,{ref:"overflowRef",onUpdateOverflow:this.onUpdateOverflow,getCounter:this.getCounter,onUpdateCount:this.onUpdateCount,updateCounter:this.updateCounter,style:{width:"100%",display:"flex",overflow:"hidden"}},{default:a,counter:this.renderCounter}):a());return m?s(Qe,{onResize:this.onResize},{default:p}):p()}}),Vo={class:"logout"},Do=B({__name:"MainLayout",setup(e){const r=so(),o=co(),n=[{label:"状态看板",key:"/"},{label:"规则管理",key:"/rules"},{label:"配置",key:"/config"},{label:"音色",key:"/tts"},{label:"日志",key:"/logs"}],a=C(()=>r.path);function l(p){o.push(p)}function m(){lo(),o.push("/login")}return(p,d)=>{const h=ao("router-view");return ro(),to(Y(Ao),{"has-sider":"",style:{height:"100vh"}},{default:Q(()=>[W(Y(_o),{bordered:"",width:210,style:{display:"flex","flex-direction":"column"}},{default:Q(()=>[d[1]||(d[1]=ye("div",{class:"logo"},"homeService 控制台",-1)),W(Y(Ko),{value:a.value,options:n,"onUpdate:value":l},null,8,["value"]),ye("div",Vo,[W(Y(no),{text:"",type:"error",onClick:m},{default:Q(()=>[...d[0]||(d[0]=[io("退出登录",-1)])]),_:1})])]),_:1}),W(Y(Ho),{"content-style":"padding: 16px; box-sizing: border-box","native-scrollbar":!1},{default:Q(()=>[W(h)]),_:1})]),_:1})}}}),Qo=bo(Do,[["__scopeId","data-v-74cd67c3"]]);export{Qo as default};
