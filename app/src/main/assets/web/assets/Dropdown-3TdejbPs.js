import{p as me,N as be,a as de,B as _e,b as Te,d as $e,h as fe,r as De,c as Be}from"./Popover-CdykbPzp.js";import{aW as je,aV as Ae,ak as q,an as ae,aX as Fe,aY as He,a0 as Me,ax as G,r as D,d as I,h as s,i as ge,k as le,m as Le,l as We,t as se,v as W,z as b,n as ce,K as Z,F,p as R,q as k,D as $,az as we,U as ue,y as ye,aD as Ee,M as X,aK as xe,A as L,O as Ue,aZ as Ve,aL as qe,aJ as Ge,aC as Xe,I as he,C as T,P as Ze,G as re,H as K,am as A}from"./index-WfdfZwQy.js";import{f as Je}from"./get-DsIbwzAk.js";import{u as Ye}from"./use-merged-state-CFAGMfNR.js";function Qe(e={},o){const r=Ae({ctrl:!1,command:!1,win:!1,shift:!1,tab:!1}),{keydown:t,keyup:i}=e,n=a=>{switch(a.key){case"Control":r.ctrl=!0;break;case"Meta":r.command=!0,r.win=!0;break;case"Shift":r.shift=!0;break;case"Tab":r.tab=!0;break}t!==void 0&&Object.keys(t).forEach(g=>{if(g!==a.key)return;const v=t[g];if(typeof v=="function")v(a);else{const{stop:y=!1,prevent:x=!1}=v;y&&a.stopPropagation(),x&&a.preventDefault(),v.handler(a)}})},l=a=>{switch(a.key){case"Control":r.ctrl=!1;break;case"Meta":r.command=!1,r.win=!1;break;case"Shift":r.shift=!1;break;case"Tab":r.tab=!1;break}i!==void 0&&Object.keys(i).forEach(g=>{if(g!==a.key)return;const v=i[g];if(typeof v=="function")v(a);else{const{stop:y=!1,prevent:x=!1}=v;y&&a.stopPropagation(),x&&a.preventDefault(),v.handler(a)}})},u=()=>{(o===void 0||o.value)&&(q("keydown",document,n),q("keyup",document,l)),o!==void 0&&ae(o,a=>{a?(q("keydown",document,n),q("keyup",document,l)):(G("keydown",document,n),G("keyup",document,l))})};return Fe()?(He(u),Me(()=>{(o===void 0||o.value)&&(G("keydown",document,n),G("keyup",document,l))})):u(),je(r)}function eo(e,o,r){const t=D(e.value);let i=null;return ae(e,n=>{i!==null&&window.clearTimeout(i),n===!0?r&&!r.value?t.value=!0:i=window.setTimeout(()=>{t.value=!0},o):t.value=!1}),t}function oo(e){return o=>{o?e.value=o.$el:e.value=null}}const no=I({name:"ChevronRight",render(){return s("svg",{viewBox:"0 0 16 16",fill:"none",xmlns:"http://www.w3.org/2000/svg"},s("path",{d:"M5.64645 3.14645C5.45118 3.34171 5.45118 3.65829 5.64645 3.85355L9.79289 8L5.64645 12.1464C5.45118 12.3417 5.45118 12.6583 5.64645 12.8536C5.84171 13.0488 6.15829 13.0488 6.35355 12.8536L10.8536 8.35355C11.0488 8.15829 11.0488 7.84171 10.8536 7.64645L6.35355 3.14645C6.15829 2.95118 5.84171 2.95118 5.64645 3.14645Z",fill:"currentColor"}))}}),to={padding:"4px 0",optionIconSizeSmall:"14px",optionIconSizeMedium:"16px",optionIconSizeLarge:"16px",optionIconSizeHuge:"18px",optionSuffixWidthSmall:"14px",optionSuffixWidthMedium:"14px",optionSuffixWidthLarge:"16px",optionSuffixWidthHuge:"16px",optionIconSuffixWidthSmall:"32px",optionIconSuffixWidthMedium:"32px",optionIconSuffixWidthLarge:"36px",optionIconSuffixWidthHuge:"36px",optionPrefixWidthSmall:"14px",optionPrefixWidthMedium:"14px",optionPrefixWidthLarge:"16px",optionPrefixWidthHuge:"16px",optionIconPrefixWidthSmall:"36px",optionIconPrefixWidthMedium:"36px",optionIconPrefixWidthLarge:"40px",optionIconPrefixWidthHuge:"40px"};function ro(e){const{primaryColor:o,textColor2:r,dividerColor:t,hoverColor:i,popoverColor:n,invertedColor:l,borderRadius:u,fontSizeSmall:a,fontSizeMedium:g,fontSizeLarge:v,fontSizeHuge:y,heightSmall:x,heightMedium:P,heightLarge:C,heightHuge:O,textColor3:S,opacityDisabled:N}=e;return Object.assign(Object.assign({},to),{optionHeightSmall:x,optionHeightMedium:P,optionHeightLarge:C,optionHeightHuge:O,borderRadius:u,fontSizeSmall:a,fontSizeMedium:g,fontSizeLarge:v,fontSizeHuge:y,optionTextColor:r,optionTextColorHover:r,optionTextColorActive:o,optionTextColorChildActive:o,color:n,dividerColor:t,suffixColor:r,prefixColor:r,optionColorHover:i,optionColorActive:Le(o,{alpha:.1}),groupHeaderTextColor:S,optionTextColorInverted:"#BBB",optionTextColorHoverInverted:"#FFF",optionTextColorActiveInverted:"#FFF",optionTextColorChildActiveInverted:"#FFF",colorInverted:l,dividerColorInverted:"#BBB",suffixColorInverted:"#BBB",prefixColorInverted:"#BBB",optionColorHoverInverted:o,optionColorActiveInverted:o,groupHeaderTextColorInverted:"#AAA",optionOpacityDisabled:N})}const io=ge({name:"Dropdown",common:le,peers:{Popover:me},self:ro}),ao={padding:"8px 14px"};function lo(e){const{borderRadius:o,boxShadow2:r,baseColor:t}=e;return Object.assign(Object.assign({},ao),{borderRadius:o,boxShadow:r,color:We(t,"rgba(0, 0, 0, .85)"),textColor:t})}const so=ge({name:"Tooltip",common:le,peers:{Popover:me},self:lo}),co=Object.assign(Object.assign({},de),W.props),Oo=I({name:"Tooltip",props:co,slots:Object,__popover__:!0,setup(e){const{mergedClsPrefixRef:o}=se(e),r=W("Tooltip","-tooltip",void 0,so,e,o),t=D(null);return Object.assign(Object.assign({},{syncPosition(){t.value.syncPosition()},setShow(n){t.value.setShow(n)}}),{popoverRef:t,mergedTheme:r,popoverThemeOverrides:b(()=>r.value.self)})},render(){const{mergedTheme:e,internalExtraClass:o}=this;return s(be,Object.assign(Object.assign({},this.$props),{theme:e.peers.Popover,themeOverrides:e.peerOverrides.Popover,builtinThemeOverrides:this.popoverThemeOverrides,internalExtraClass:o.concat("tooltip"),ref:"popoverRef"}),this.$slots)}}),pe=ce("n-dropdown-menu"),J=ce("n-dropdown"),ve=ce("n-dropdown-option"),Se=I({name:"DropdownDivider",props:{clsPrefix:{type:String,required:!0}},render(){return s("div",{class:`${this.clsPrefix}-dropdown-divider`})}}),uo=I({name:"DropdownGroupHeader",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0}},setup(){const{showIconRef:e,hasSubmenuRef:o}=F(pe),{renderLabelRef:r,labelFieldRef:t,nodePropsRef:i,renderOptionRef:n}=F(J);return{labelField:t,showIcon:e,hasSubmenu:o,renderLabel:r,nodeProps:i,renderOption:n}},render(){var e;const{clsPrefix:o,hasSubmenu:r,showIcon:t,nodeProps:i,renderLabel:n,renderOption:l}=this,{rawNode:u}=this.tmNode,a=s("div",Object.assign({class:`${o}-dropdown-option`},i?.(u)),s("div",{class:`${o}-dropdown-option-body ${o}-dropdown-option-body--group`},s("div",{"data-dropdown-option":!0,class:[`${o}-dropdown-option-body__prefix`,t&&`${o}-dropdown-option-body__prefix--show-icon`]},Z(u.icon)),s("div",{class:`${o}-dropdown-option-body__label`,"data-dropdown-option":!0},n?n(u):Z((e=u.title)!==null&&e!==void 0?e:u[this.labelField])),s("div",{class:[`${o}-dropdown-option-body__suffix`,r&&`${o}-dropdown-option-body__suffix--has-submenu`],"data-dropdown-option":!0})));return l?l({node:a,option:u}):a}});function po(e){const{textColorBase:o,opacity1:r,opacity2:t,opacity3:i,opacity4:n,opacity5:l}=e;return{color:o,opacity1Depth:r,opacity2Depth:t,opacity3Depth:i,opacity4Depth:n,opacity5Depth:l}}const fo={common:le,self:po},ho=R("icon",`
 height: 1em;
 width: 1em;
 line-height: 1em;
 text-align: center;
 display: inline-block;
 position: relative;
 fill: currentColor;
`,[k("color-transition",{transition:"color .3s var(--n-bezier)"}),k("depth",{color:"var(--n-color)"},[$("svg",{opacity:"var(--n-opacity)",transition:"opacity .3s var(--n-bezier)"})]),$("svg",{height:"1em",width:"1em"})]),vo=Object.assign(Object.assign({},W.props),{depth:[String,Number],size:[Number,String],color:String,component:[Object,Function]}),mo=I({_n_icon__:!0,name:"Icon",inheritAttrs:!1,props:vo,setup(e){const{mergedClsPrefixRef:o,inlineThemeDisabled:r}=se(e),t=W("Icon","-icon",ho,fo,e,o),i=b(()=>{const{depth:l}=e,{common:{cubicBezierEaseInOut:u},self:a}=t.value;if(l!==void 0){const{color:g,[`opacity${l}Depth`]:v}=a;return{"--n-bezier":u,"--n-color":g,"--n-opacity":v}}return{"--n-bezier":u,"--n-color":"","--n-opacity":""}}),n=r?ye("icon",b(()=>`${e.depth||"d"}`),i,e):void 0;return{mergedClsPrefix:o,mergedStyle:b(()=>{const{size:l,color:u}=e;return{fontSize:Je(l),color:u}}),cssVars:r?void 0:i,themeClass:n?.themeClass,onRender:n?.onRender}},render(){var e;const{$parent:o,depth:r,mergedClsPrefix:t,component:i,onRender:n,themeClass:l}=this;return!((e=o?.$options)===null||e===void 0)&&e._n_icon__&&we("icon","don't wrap `n-icon` inside `n-icon`"),n?.(),s("i",ue(this.$attrs,{role:"img",class:[`${t}-icon`,l,{[`${t}-icon--depth`]:r,[`${t}-icon--color-transition`]:r!==void 0}],style:[this.cssVars,this.mergedStyle]}),i?s(i):this.$slots)}});function ie(e,o){return e.type==="submenu"||e.type===void 0&&e[o]!==void 0}function bo(e){return e.type==="group"}function Pe(e){return e.type==="divider"}function go(e){return e.type==="render"}const Ce=I({name:"DropdownOption",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0},parentKey:{type:[String,Number],default:null},placement:{type:String,default:"right-start"},props:Object,scrollable:Boolean},setup(e){const o=F(J),{hoverKeyRef:r,keyboardKeyRef:t,lastToggledSubmenuKeyRef:i,pendingKeyPathRef:n,activeKeyPathRef:l,animatedRef:u,mergedShowRef:a,renderLabelRef:g,renderIconRef:v,labelFieldRef:y,childrenFieldRef:x,renderOptionRef:P,nodePropsRef:C,menuPropsRef:O}=o,S=F(ve,null),N=F(pe),_=F(xe),U=b(()=>e.tmNode.rawNode),E=b(()=>{const{value:d}=x;return ie(e.tmNode.rawNode,d)}),Y=b(()=>{const{disabled:d}=e.tmNode;return d}),Q=b(()=>{if(!E.value)return!1;const{key:d,disabled:f}=e.tmNode;if(f)return!1;const{value:w}=r,{value:B}=t,{value:te}=i,{value:j}=n;return w!==null?j.includes(d):B!==null?j.includes(d)&&j[j.length-1]!==d:te!==null?j.includes(d):!1}),ee=b(()=>t.value===null&&!u.value),oe=eo(Q,300,ee),ne=b(()=>!!S?.enteringSubmenuRef.value),H=D(!1);L(ve,{enteringSubmenuRef:H});function M(){H.value=!0}function V(){H.value=!1}function z(){const{parentKey:d,tmNode:f}=e;f.disabled||a.value&&(i.value=d,t.value=null,r.value=f.key)}function c(){const{tmNode:d}=e;d.disabled||a.value&&r.value!==d.key&&z()}function p(d){if(e.tmNode.disabled||!a.value)return;const{relatedTarget:f}=d;f&&!fe({target:f},"dropdownOption")&&!fe({target:f},"scrollbarRail")&&(r.value=null)}function h(){const{value:d}=E,{tmNode:f}=e;a.value&&!d&&!f.disabled&&(o.doSelect(f.key,f.rawNode),o.doUpdateShow(!1))}return{labelField:y,renderLabel:g,renderIcon:v,siblingHasIcon:N.showIconRef,siblingHasSubmenu:N.hasSubmenuRef,menuProps:O,popoverBody:_,animated:u,mergedShowSubmenu:b(()=>oe.value&&!ne.value),rawNode:U,hasSubmenu:E,pending:X(()=>{const{value:d}=n,{key:f}=e.tmNode;return d.includes(f)}),childActive:X(()=>{const{value:d}=l,{key:f}=e.tmNode,w=d.findIndex(B=>f===B);return w===-1?!1:w<d.length-1}),active:X(()=>{const{value:d}=l,{key:f}=e.tmNode,w=d.findIndex(B=>f===B);return w===-1?!1:w===d.length-1}),mergedDisabled:Y,renderOption:P,nodeProps:C,handleClick:h,handleMouseMove:c,handleMouseEnter:z,handleMouseLeave:p,handleSubmenuBeforeEnter:M,handleSubmenuAfterEnter:V}},render(){var e,o;const{animated:r,rawNode:t,mergedShowSubmenu:i,clsPrefix:n,siblingHasIcon:l,siblingHasSubmenu:u,renderLabel:a,renderIcon:g,renderOption:v,nodeProps:y,props:x,scrollable:P}=this;let C=null;if(i){const _=(e=this.menuProps)===null||e===void 0?void 0:e.call(this,t,t.children);C=s(Re,Object.assign({},_,{clsPrefix:n,scrollable:this.scrollable,tmNodes:this.tmNode.children,parentKey:this.tmNode.key}))}const O={class:[`${n}-dropdown-option-body`,this.pending&&`${n}-dropdown-option-body--pending`,this.active&&`${n}-dropdown-option-body--active`,this.childActive&&`${n}-dropdown-option-body--child-active`,this.mergedDisabled&&`${n}-dropdown-option-body--disabled`],onMousemove:this.handleMouseMove,onMouseenter:this.handleMouseEnter,onMouseleave:this.handleMouseLeave,onClick:this.handleClick},S=y?.(t),N=s("div",Object.assign({class:[`${n}-dropdown-option`,S?.class],"data-dropdown-option":!0},S),s("div",ue(O,x),[s("div",{class:[`${n}-dropdown-option-body__prefix`,l&&`${n}-dropdown-option-body__prefix--show-icon`]},[g?g(t):Z(t.icon)]),s("div",{"data-dropdown-option":!0,class:`${n}-dropdown-option-body__label`},a?a(t):Z((o=t[this.labelField])!==null&&o!==void 0?o:t.title)),s("div",{"data-dropdown-option":!0,class:[`${n}-dropdown-option-body__suffix`,u&&`${n}-dropdown-option-body__suffix--has-submenu`]},this.hasSubmenu?s(mo,null,{default:()=>s(no,null)}):null)]),this.hasSubmenu?s(_e,null,{default:()=>[s(Te,null,{default:()=>s("div",{class:`${n}-dropdown-offset-container`},s($e,{show:this.mergedShowSubmenu,placement:this.placement,to:P&&this.popoverBody||void 0,teleportDisabled:!P},{default:()=>s("div",{class:`${n}-dropdown-menu-wrapper`},r?s(Ee,{onBeforeEnter:this.handleSubmenuBeforeEnter,onAfterEnter:this.handleSubmenuAfterEnter,name:"fade-in-scale-up-transition",appear:!0},{default:()=>C}):C)}))})]}):null);return v?v({node:N,option:t}):N}}),wo=I({name:"NDropdownGroup",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0},parentKey:{type:[String,Number],default:null}},render(){const{tmNode:e,parentKey:o,clsPrefix:r}=this,{children:t}=e;return s(Ue,null,s(uo,{clsPrefix:r,tmNode:e,key:e.key}),t?.map(i=>{const{rawNode:n}=i;return n.show===!1?null:Pe(n)?s(Se,{clsPrefix:r,key:i.key}):i.isGroup?(we("dropdown","`group` node is not allowed to be put in `group` node."),null):s(Ce,{clsPrefix:r,tmNode:i,parentKey:o,key:i.key})}))}}),yo=I({name:"DropdownRenderOption",props:{tmNode:{type:Object,required:!0}},render(){const{rawNode:{render:e,props:o}}=this.tmNode;return s("div",o,[e?.()])}}),Re=I({name:"DropdownMenu",props:{scrollable:Boolean,showArrow:Boolean,arrowStyle:[String,Object],clsPrefix:{type:String,required:!0},tmNodes:{type:Array,default:()=>[]},parentKey:{type:[String,Number],default:null}},setup(e){const{renderIconRef:o,childrenFieldRef:r}=F(J);L(pe,{showIconRef:b(()=>{const i=o.value;return e.tmNodes.some(n=>{var l;if(n.isGroup)return(l=n.children)===null||l===void 0?void 0:l.some(({rawNode:a})=>i?i(a):a.icon);const{rawNode:u}=n;return i?i(u):u.icon})}),hasSubmenuRef:b(()=>{const{value:i}=r;return e.tmNodes.some(n=>{var l;if(n.isGroup)return(l=n.children)===null||l===void 0?void 0:l.some(({rawNode:a})=>ie(a,i));const{rawNode:u}=n;return ie(u,i)})})});const t=D(null);return L(qe,null),L(Ge,null),L(xe,t),{bodyRef:t}},render(){const{parentKey:e,clsPrefix:o,scrollable:r}=this,t=this.tmNodes.map(i=>{const{rawNode:n}=i;return n.show===!1?null:go(n)?s(yo,{tmNode:i,key:i.key}):Pe(n)?s(Se,{clsPrefix:o,key:i.key}):bo(n)?s(wo,{clsPrefix:o,tmNode:i,parentKey:e,key:i.key}):s(Ce,{clsPrefix:o,tmNode:i,parentKey:e,key:i.key,props:n.props,scrollable:r})});return s("div",{class:[`${o}-dropdown-menu`,r&&`${o}-dropdown-menu--scrollable`],ref:"bodyRef"},r?s(Ve,{contentClass:`${o}-dropdown-menu__content`},{default:()=>t}):t,this.showArrow?De({clsPrefix:o,arrowStyle:this.arrowStyle,arrowClass:void 0,arrowWrapperClass:void 0,arrowWrapperStyle:void 0}):null)}}),xo=R("dropdown-menu",`
 transform-origin: var(--v-transform-origin);
 background-color: var(--n-color);
 border-radius: var(--n-border-radius);
 box-shadow: var(--n-box-shadow);
 position: relative;
 transition:
 background-color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier);
`,[Xe(),R("dropdown-option",`
 position: relative;
 `,[$("a",`
 text-decoration: none;
 color: inherit;
 outline: none;
 `,[$("&::before",`
 content: "";
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `)]),R("dropdown-option-body",`
 display: flex;
 cursor: pointer;
 position: relative;
 height: var(--n-option-height);
 line-height: var(--n-option-height);
 font-size: var(--n-font-size);
 color: var(--n-option-text-color);
 transition: color .3s var(--n-bezier);
 `,[$("&::before",`
 content: "";
 position: absolute;
 top: 0;
 bottom: 0;
 left: 4px;
 right: 4px;
 transition: background-color .3s var(--n-bezier);
 border-radius: var(--n-border-radius);
 `),he("disabled",[k("pending",`
 color: var(--n-option-text-color-hover);
 `,[T("prefix, suffix",`
 color: var(--n-option-text-color-hover);
 `),$("&::before","background-color: var(--n-option-color-hover);")]),k("active",`
 color: var(--n-option-text-color-active);
 `,[T("prefix, suffix",`
 color: var(--n-option-text-color-active);
 `),$("&::before","background-color: var(--n-option-color-active);")]),k("child-active",`
 color: var(--n-option-text-color-child-active);
 `,[T("prefix, suffix",`
 color: var(--n-option-text-color-child-active);
 `)])]),k("disabled",`
 cursor: not-allowed;
 opacity: var(--n-option-opacity-disabled);
 `),k("group",`
 font-size: calc(var(--n-font-size) - 1px);
 color: var(--n-group-header-text-color);
 `,[T("prefix",`
 width: calc(var(--n-option-prefix-width) / 2);
 `,[k("show-icon",`
 width: calc(var(--n-option-icon-prefix-width) / 2);
 `)])]),T("prefix",`
 width: var(--n-option-prefix-width);
 display: flex;
 justify-content: center;
 align-items: center;
 color: var(--n-prefix-color);
 transition: color .3s var(--n-bezier);
 z-index: 1;
 `,[k("show-icon",`
 width: var(--n-option-icon-prefix-width);
 `),R("icon",`
 font-size: var(--n-option-icon-size);
 `)]),T("label",`
 white-space: nowrap;
 flex: 1;
 z-index: 1;
 `),T("suffix",`
 box-sizing: border-box;
 flex-grow: 0;
 flex-shrink: 0;
 display: flex;
 justify-content: flex-end;
 align-items: center;
 min-width: var(--n-option-suffix-width);
 padding: 0 8px;
 transition: color .3s var(--n-bezier);
 color: var(--n-suffix-color);
 z-index: 1;
 `,[k("has-submenu",`
 width: var(--n-option-icon-suffix-width);
 `),R("icon",`
 font-size: var(--n-option-icon-size);
 `)]),R("dropdown-menu","pointer-events: all;")]),R("dropdown-offset-container",`
 pointer-events: none;
 position: absolute;
 left: 0;
 right: 0;
 top: -4px;
 bottom: -4px;
 `)]),R("dropdown-divider",`
 transition: background-color .3s var(--n-bezier);
 background-color: var(--n-divider-color);
 height: 1px;
 margin: 4px 0;
 `),R("dropdown-menu-wrapper",`
 transform-origin: var(--v-transform-origin);
 width: fit-content;
 `),$(">",[R("scrollbar",`
 height: inherit;
 max-height: inherit;
 `)]),he("scrollable",`
 padding: var(--n-padding);
 `),k("scrollable",[T("content",`
 padding: var(--n-padding);
 `)])]),So={animated:{type:Boolean,default:!0},keyboard:{type:Boolean,default:!0},size:String,inverted:Boolean,placement:{type:String,default:"bottom"},onSelect:[Function,Array],options:{type:Array,default:()=>[]},menuProps:Function,showArrow:Boolean,renderLabel:Function,renderIcon:Function,renderOption:Function,nodeProps:Function,labelField:{type:String,default:"label"},keyField:{type:String,default:"key"},childrenField:{type:String,default:"children"},value:[String,Number]},Po=Object.keys(de),Co=Object.assign(Object.assign(Object.assign({},de),So),W.props),zo=I({name:"Dropdown",inheritAttrs:!1,props:Co,setup(e){const o=D(!1),r=Ye(K(e,"show"),o),t=b(()=>{const{keyField:c,childrenField:p}=e;return Be(e.options,{getKey(h){return h[c]},getDisabled(h){return h.disabled===!0},getIgnored(h){return h.type==="divider"||h.type==="render"},getChildren(h){return h[p]}})}),i=b(()=>t.value.treeNodes),n=D(null),l=D(null),u=D(null),a=b(()=>{var c,p,h;return(h=(p=(c=n.value)!==null&&c!==void 0?c:l.value)!==null&&p!==void 0?p:u.value)!==null&&h!==void 0?h:null}),g=b(()=>t.value.getPath(a.value).keyPath),v=b(()=>t.value.getPath(e.value).keyPath),y=X(()=>e.keyboard&&r.value);Qe({keydown:{ArrowUp:{prevent:!0,handler:ee},ArrowRight:{prevent:!0,handler:Q},ArrowDown:{prevent:!0,handler:oe},ArrowLeft:{prevent:!0,handler:Y},Enter:{prevent:!0,handler:ne},Escape:E}},y);const{mergedClsPrefixRef:x,inlineThemeDisabled:P,mergedComponentPropsRef:C}=se(e),O=b(()=>{var c,p;return e.size||((p=(c=C?.value)===null||c===void 0?void 0:c.Dropdown)===null||p===void 0?void 0:p.size)||"medium"}),S=W("Dropdown","-dropdown",xo,io,e,x);L(J,{labelFieldRef:K(e,"labelField"),childrenFieldRef:K(e,"childrenField"),renderLabelRef:K(e,"renderLabel"),renderIconRef:K(e,"renderIcon"),hoverKeyRef:n,keyboardKeyRef:l,lastToggledSubmenuKeyRef:u,pendingKeyPathRef:g,activeKeyPathRef:v,animatedRef:K(e,"animated"),mergedShowRef:r,nodePropsRef:K(e,"nodeProps"),renderOptionRef:K(e,"renderOption"),menuPropsRef:K(e,"menuProps"),doSelect:N,doUpdateShow:_}),ae(r,c=>{!e.animated&&!c&&U()});function N(c,p){const{onSelect:h}=e;h&&re(h,c,p)}function _(c){const{"onUpdate:show":p,onUpdateShow:h}=e;p&&re(p,c),h&&re(h,c),o.value=c}function U(){n.value=null,l.value=null,u.value=null}function E(){_(!1)}function Y(){M("left")}function Q(){M("right")}function ee(){M("up")}function oe(){M("down")}function ne(){const c=H();c?.isLeaf&&r.value&&(N(c.key,c.rawNode),_(!1))}function H(){var c;const{value:p}=t,{value:h}=a;return!p||h===null?null:(c=p.getNode(h))!==null&&c!==void 0?c:null}function M(c){const{value:p}=a,{value:{getFirstAvailableNode:h}}=t;let d=null;if(p===null){const f=h();f!==null&&(d=f.key)}else{const f=H();if(f){let w;switch(c){case"down":w=f.getNext();break;case"up":w=f.getPrev();break;case"right":w=f.getChild();break;case"left":w=f.getParent();break}w&&(d=w.key)}}d!==null&&(n.value=null,l.value=d)}const V=b(()=>{const{inverted:c}=e,p=O.value,{common:{cubicBezierEaseInOut:h},self:d}=S.value,{padding:f,dividerColor:w,borderRadius:B,optionOpacityDisabled:te,[A("optionIconSuffixWidth",p)]:j,[A("optionSuffixWidth",p)]:ke,[A("optionIconPrefixWidth",p)]:Ne,[A("optionPrefixWidth",p)]:Ie,[A("fontSize",p)]:Oe,[A("optionHeight",p)]:ze,[A("optionIconSize",p)]:Ke}=d,m={"--n-bezier":h,"--n-font-size":Oe,"--n-padding":f,"--n-border-radius":B,"--n-option-height":ze,"--n-option-prefix-width":Ie,"--n-option-icon-prefix-width":Ne,"--n-option-suffix-width":ke,"--n-option-icon-suffix-width":j,"--n-option-icon-size":Ke,"--n-divider-color":w,"--n-option-opacity-disabled":te};return c?(m["--n-color"]=d.colorInverted,m["--n-option-color-hover"]=d.optionColorHoverInverted,m["--n-option-color-active"]=d.optionColorActiveInverted,m["--n-option-text-color"]=d.optionTextColorInverted,m["--n-option-text-color-hover"]=d.optionTextColorHoverInverted,m["--n-option-text-color-active"]=d.optionTextColorActiveInverted,m["--n-option-text-color-child-active"]=d.optionTextColorChildActiveInverted,m["--n-prefix-color"]=d.prefixColorInverted,m["--n-suffix-color"]=d.suffixColorInverted,m["--n-group-header-text-color"]=d.groupHeaderTextColorInverted):(m["--n-color"]=d.color,m["--n-option-color-hover"]=d.optionColorHover,m["--n-option-color-active"]=d.optionColorActive,m["--n-option-text-color"]=d.optionTextColor,m["--n-option-text-color-hover"]=d.optionTextColorHover,m["--n-option-text-color-active"]=d.optionTextColorActive,m["--n-option-text-color-child-active"]=d.optionTextColorChildActive,m["--n-prefix-color"]=d.prefixColor,m["--n-suffix-color"]=d.suffixColor,m["--n-group-header-text-color"]=d.groupHeaderTextColor),m}),z=P?ye("dropdown",b(()=>`${O.value[0]}${e.inverted?"i":""}`),V,e):void 0;return{mergedClsPrefix:x,mergedTheme:S,mergedSize:O,tmNodes:i,mergedShow:r,handleAfterLeave:()=>{e.animated&&U()},doUpdateShow:_,cssVars:P?void 0:V,themeClass:z?.themeClass,onRender:z?.onRender}},render(){const e=(t,i,n,l,u)=>{var a;const{mergedClsPrefix:g,menuProps:v}=this;(a=this.onRender)===null||a===void 0||a.call(this);const y=v?.(void 0,this.tmNodes.map(P=>P.rawNode))||{},x={ref:oo(i),class:[t,`${g}-dropdown`,`${g}-dropdown--${this.mergedSize}-size`,this.themeClass],clsPrefix:g,tmNodes:this.tmNodes,style:[...n,this.cssVars],showArrow:this.showArrow,arrowStyle:this.arrowStyle,scrollable:this.scrollable,onMouseenter:l,onMouseleave:u};return s(Re,ue(this.$attrs,x,y))},{mergedTheme:o}=this,r={show:this.mergedShow,theme:o.peers.Popover,themeOverrides:o.peerOverrides.Popover,internalOnAfterLeave:this.handleAfterLeave,internalRenderBody:e,onUpdateShow:this.doUpdateShow,"onUpdate:show":void 0};return s(be,Object.assign({},Ze(this.$props,Po),r),{trigger:()=>{var t,i;return(i=(t=this.$slots).default)===null||i===void 0?void 0:i.call(t)}})}});export{no as C,Oo as N,zo as a,oo as c,io as d,so as t};
