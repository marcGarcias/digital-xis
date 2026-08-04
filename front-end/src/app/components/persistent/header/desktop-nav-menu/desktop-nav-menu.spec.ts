import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DesktopNavMenu } from './desktop-nav-menu';

describe('DesktopNavMenu', () => {
  let component: DesktopNavMenu;
  let fixture: ComponentFixture<DesktopNavMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DesktopNavMenu]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DesktopNavMenu);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
