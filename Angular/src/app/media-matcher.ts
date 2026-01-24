import { Inject, Injectable } from '@angular/core';
import { Platform } from '@angular/cdk/platform';
import { CustomDomSharedStylesHost } from './shared_styles_host';

const mediaQueriesForWebkitCompatibility: Set<string> = new Set<string>();

let mediaQueryStyleNode: HTMLStyleElement | undefined;

@Injectable()
export class CustomMediaMatcher {
    private _matchMedia: (query: string) => MediaQueryList;

    constructor(private _platform: Platform, private customDomSharedStylesHost: CustomDomSharedStylesHost) {
        this._matchMedia =
            this._platform.isBrowser && window.matchMedia
                ? 
                window.matchMedia.bind(window)
                : noopMatchMedia;
    }

    matchMedia(query: string): MediaQueryList {
        if (this._platform.WEBKIT || this._platform.BLINK) {
            createEmptyStyleRule(query, this.customDomSharedStylesHost._nonce);
        }
        return this._matchMedia(query);
    }
}

function createEmptyStyleRule(query: string, nonce: string | null | undefined) {
    if (mediaQueriesForWebkitCompatibility.has(query)) {
        return;
    }

    try {
        if (!mediaQueryStyleNode) {
            mediaQueryStyleNode = document.createElement('style');
            mediaQueryStyleNode.setAttribute('type', 'text/css');
            if (!!nonce) {
                mediaQueryStyleNode.setAttribute('nonce', nonce);
            }
            document.head!.appendChild(mediaQueryStyleNode);
        }

        if (mediaQueryStyleNode.sheet) {
            mediaQueryStyleNode.sheet.insertRule(`@media ${query} {body{ }}`, 0);
            mediaQueriesForWebkitCompatibility.add(query);
        }
    } catch (e) {
        console.error(e);
    }
}

function noopMatchMedia(query: string): MediaQueryList {
    return {
        matches: query === 'all' || query === '',
        media: query,
        addListener: () => { },
        removeListener: () => { },
    } as any;
}