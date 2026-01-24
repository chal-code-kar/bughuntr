import { DOCUMENT, ɵgetDOM as getDOM } from '@angular/common';
import { Inject, Injectable, OnDestroy } from '@angular/core';
import { ɵSharedStylesHost } from '@angular/platform-browser';

@Injectable()
export class CustomDomSharedStylesHost
  extends ɵSharedStylesHost
  implements OnDestroy
{
  private _hostNodes = new Map<Node, Node[]>();
  _nonce: string | null | undefined = null;

  constructor(
    @Inject(DOCUMENT) private _doc: any,
    @Inject('cspMetaSelector')
    private _metaCSPTag: string
  ) {
    super(_doc,_metaCSPTag);
    this._hostNodes.set(_doc.head, []);
    this._setCSPNonce();
  }

  private _addStylesToHost(
    styles: Set<string>,
    host: Node,
    styleNodes: Node[]
  ): void {
    styles.forEach((style: string) => {
      const styleEl = this._doc.createElement('style');
 
      styleEl.textContent = style;

      if (!style.includes('without-nonce') && this._nonce) {
        styleEl.setAttribute('nonce', this._nonce);
      }

      styleNodes.push(host.appendChild(styleEl));
    });

    if (this._nonce) {
      this._removeCSPNonceHeader();
    }
  }

  private _setCSPNonce(): void {
    this._nonce = document
      .querySelector(this._metaCSPTag)
      ?.getAttribute('content');
  }

  private _removeCSPNonceHeader(): void {
    document.querySelector(this._metaCSPTag)?.remove();
  }

  addHost(hostNode: Node): void {
    const styleNodes: Node[] = [];
    this._hostNodes.set(hostNode, styleNodes);
  }

  removeHost(hostNode: Node): void {
    const styleNodes = this._hostNodes.get(hostNode);
    if (styleNodes) {
      styleNodes.forEach(removeStyle);
    }
    this._hostNodes.delete(hostNode);
  }

  onStylesAdded(additions: Set<string>): void {
    this._hostNodes.forEach((styleNodes, hostNode) => {
      this._addStylesToHost(additions, hostNode, styleNodes);
    });
  }

  ngOnDestroy(): void {
    this._hostNodes.forEach((styleNodes) => styleNodes.forEach(removeStyle));
  }
}

function removeStyle(styleNode: Node): void {
  getDOM().remove(styleNode);
}